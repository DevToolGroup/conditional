import { Col, DatePicker, Form, Input, Modal, Row, Select, SelectProps } from "antd";
import { useState } from "react";

interface Option {
  value: string
  label: string
}

const Types: Option[] = [
  { value: 'time', label: '时间' },
  { value: 'string', label: '字符' },
  { value: 'number', label: '数值' },
  { value: 'argument', label: '参数' },
  { value: 'variable', label: '变量' },
]

const arithValueOpts: Option[] = [
  { value: 'argument', label: '参数' },
  { value: 'variable', label: '变量' },
  { value: 'number', label: '数值' },
]

const compareValueOpts: Option[] = [
  { value: 'argument', label: '参数' },
  { value: 'variable', label: '变量' },
  { value: 'number', label: '数值' },
]

const logicValueOpts: Option[] = [
  { value: 'argument', label: '参数' },
  { value: 'variable', label: '变量' },
]

const variableOpts: Option[] = [
  { value: 'compare', label: '比较' },
  { value: 'logic', label: '逻辑' },
  { value: 'arith', label: '运算' },
  { value: 'function', label: '函数' },
]

const conditionOpts: Option[] = [
  { value: 'argument', label: '参数' },
  { value: 'variable', label: '变量' },
  { value: 'compare', label: '比较' },
  { value: 'function', label: '函数' },
]

const actionOpts: Option[] = [
  { value: 'function', label: '函数' },
]

const logic: Option[] = [
  { value: '&&', label: '且' },
  { value: '||', label: '或' },
]

const arith: Option[] = [
  { value: '+', label: '加法' },
  { value: '-', label: '减法' },
  { value: '*', label: '乘法' },
  { value: '/', label: '除法' },
  { value: '%', label: '求余' },
]

const compare: Option[] = [
  {
    value: '>',
    label: '大于'
  },
  {
    value: '>=',
    label: '大于等于'
  },
  {
    value: '<',
    label: '小于'
  },
  {
    value: '<=',
    label: '小于等于'
  },
  {
    value: '==',
    label: '等于'
  },
  {
    value: '!=',
    label: '不等于'
  },
]

const defaultFunctions: Option[] = [
  { value: 'IN', label: '包含' }

]

const actionFunctions: Option[] = [
  { value: 'PUT', label: '赋值' }
]

const FunctionArguments: { [key: string]: FunctionArgument[] } = {
  IN: [
    {
      name: '包含参数',
      code: 'arg1',
      types: [
        {
          value: 'string',
          label: '字符'
        },
        {
          value: 'argument',
          label: '参数'
        },
        {
          value: 'variable',
          label: '变量'
        }
      ]
    },
    {
      code: 'arg2',
      name: '被包含参数',
      types: [
        {
          value: 'string',
          label: '字符'
        },
        {
          value: 'argument',
          label: '参数'
        },
        {
          value: 'variable',
          label: '变量'
        }
      ]
    }
  ],
  PUT: [
    {
      name: '对象',
      code: 'obj',
      types: [
        {
          value: 'variable',
          label: '变量'
        }
      ]
    },
    {
      name: '字段',
      code: 'field',
      types: [
        {
          value: 'string',
          label: '字符'
        }
      ]
    },
    {
      name: '值',
      code: 'val',
      types: [
        {
          value: 'string',
          label: '字符'
        },
        {
          value: 'number',
          label: '数值'
        },
        {
          value: 'variable',
          label: '变量'
        },
        {
          value: 'argument',
          label: '参数'
        }
      ]
    },
  ]
}

interface FunctionArgument {
  name: string;
  code: string;
  types: Option[];
}

interface ComposeData {
  op?: string
  left?: Value
  right?: Value
}

interface Arg {
  code: string
  name: string
  value: Value
}

interface FunctionData {
  func?: string
  args?: Arg[]
}

interface ExpressionData {
  name?: string
  code: string
  type?: string
  expression?: string | ComposeData | FunctionData
}

interface ExpressionModalProps {
  open: boolean
  title: string
  initValue?: ExpressionData
  types?: Option[]
  functions?: Option[]

  onClose: () => void
  onCommit: (data: ExpressionData) => void
}

interface SearchInputProps {
  id?: string
  placeholder: string
  value?: string
  onChange?: (value: string) => void
  handleSearch: (value: string, callback: (data: { value: string; text: string }[]) => void) => void
}

const SearchInput = (props: SearchInputProps) => {
  const { value, onChange, handleSearch, placeholder } = props;
  const [data, setData] = useState<SelectProps['options']>([]);

  const handleChange = (newValue: string) => {
    onChange?.(newValue);
  };

  return (
    <Select
      showSearch
      value={value}
      placeholder={placeholder}
      defaultActiveFirstOption={false}
      suffixIcon={null}
      filterOption={false}
      onSearch={(val) => handleSearch(val, setData)}
      onChange={handleChange}
      notFoundContent={null}
      options={(data || []).map((d) => ({
        value: d.value,
        label: d.text,
      }))}
    />
  );
};

interface Value {
  type?: string,
  value?: string
}

const ValueInput = ({ id, value, inputOptions, onChange }:
  {
    id?: string
    value?: Value
    inputOptions?: Option[],
    onChange: (value: Value) => void
  }) => {
  let input;
  const type = value?.type ?? 'argument'
  if (type === 'argument') {
    input = <SearchInput value={value?.value}
      handleSearch={(val, callback) => { callback([{ value: 'mock', text: 'mock' }]) }}
      placeholder="请输入参数名称"
      onChange={(val) => onChange({ ...value, value: val })} />

  } else if (type === 'variable') {
    input = <SearchInput value={value?.value}
      handleSearch={(val, callback) => { callback([{ value: 'mock', text: 'mock' }]) }}
      placeholder="请输入变量名称"
      onChange={(val) => onChange({ ...value, value: val })} />

  } else if (type === 'string') {
    input = <Input value={value?.value} onChange={(val) => onChange({ ...value, value: val.target.value })} placeholder="请输入字符"></Input>

  } else if (type === 'number') {
    input = <Input value={value?.value} onChange={(val) => onChange({ ...value, value: val.target.value })} placeholder="请输入数值"></Input>

  } else if (type === 'time') {
    input = <DatePicker showTime onChange={(val) => onChange({ ...value, value: val.toString() })} />

  }

  return (<>
    <Row gutter={8}>
      <Col span={6}>
        <Select options={inputOptions}
          value={value?.type}
          onChange={(val) => onChange?.({ ...value, type: val })}>
        </Select>
      </Col>
      <Col span={18}>
        {input}
      </Col>
    </Row>
  </>)
}

const ComposeExpressionInput = ({ value, valueOptions, operates, onChange }: {
  id?: string
  value?: ComposeData
  valueOptions?: Option[]
  operates: Option[]
  onChange?: (value: ComposeData) => void | undefined
}) => {

  return (<>
    <Row align='middle' gutter={[16, 16]}>
      <Col span={4} style={{ textAlign: 'center' }}>
        左值
      </Col>
      <Col span={20}>
        <ValueInput value={value?.left} inputOptions={valueOptions} onChange={(val) => onChange?.({ ...value, left: val })}></ValueInput>
      </Col>
      <Col span={4} style={{ textAlign: 'center' }}>操作</Col>
      <Col span={20}>
        <Select options={operates} value={value?.op} onChange={(val) => onChange?.({ ...value, op: val })}></Select>
      </Col>
      <Col span={4} style={{ textAlign: 'center' }}>右值</Col>
      <Col span={20}>
        <ValueInput value={value?.right} inputOptions={valueOptions} onChange={(val) => onChange?.({ ...value, right: val })}></ValueInput>
      </Col>
    </Row>
  </>);
}

const FunctionExpressionInput = ({ id, functions, value, onChange }: {
  id?: string
  functions: Option[]
  value?: FunctionData
  onChange?: (value: FunctionData) => void
}) => {
  const onChangeArgument = (arg: Arg): void => {
    const changedValue = { ...value }
    for (var item of changedValue.args ?? []) {
      if (item.code === arg.code) {
        item.value = arg.value;
      }
    }
    onChange?.({ ...value })
  }

  const onChangeFunction = (val: string): void => {
    const changedArguments: any[] = []
    if (val === undefined) {
      onChange?.({ ...value, func: undefined, args: changedArguments });
      return;
    }
    const param = FunctionArguments[val]
    for (var arg of param) {
      changedArguments.push({ code: arg.code, name: arg.name })
    }
    onChange?.({ ...value, func: val, args: changedArguments });
  }

  const argTypes = (paramCode: any): Option[] => {
    const result: Option[] = []
    if (value === undefined) {
      return result;
    }
    const param = FunctionArguments[value?.func ?? '']
    for (var arg of param) {
      if (arg.code === paramCode) {
        return arg.types;
      }
    }
    return result;
  }

  return (<>
    <Row gutter={[16, 24]} align='middle'>
      <Col span={4} style={{ textAlign: 'center' }}>函数名</Col>
      <Col span={20}>
        <Select allowClear={true} options={functions} value={value?.func} onChange={(val) => onChangeFunction(val)}></Select>
      </Col>
      <Col span={24} >
        <Row gutter={[8, 16]} align='middle'>
          {value && value.args && value.args.length > 0 ? <Col span={4} style={{ textAlign: 'center' }}>参数项</Col> : <></>}
          {
            value && value.args && value.args.length > 0 ? <Col span={20}>
              {
                value?.args.map((arg: Arg) => {
                  return <Row key={arg.code} align='middle' style={{ marginBottom: '16px' }}>
                    <Col span={5} style={{ textAlign: 'start' }}>
                      {arg.name}
                    </Col>
                    <Col span={19}>
                      <ValueInput id={arg.code} inputOptions={argTypes(arg.code)} value={arg.value} onChange={(val) => onChangeArgument({ ...arg, value: val })}></ValueInput>
                    </Col>
                  </Row>
                })
              }
            </Col> : <></>
          }
        </Row>
      </Col>
    </Row>
  </>)
}

const ExpressionModal = (props: ExpressionModalProps) => {
  const { open, title, initValue, types, functions, onClose, onCommit } = props;
  const [form] = Form.useForm()

  return (<>
    <Modal
      width={700} open={open} title={title}
      okText="提交" cancelText="取消"
      onCancel={() => onClose()}
      okButtonProps={{ autoFocus: true, htmlType: 'submit' }}
      modalRender={(dom) => (
        <Form form={form} name="form_in_modal"
          clearOnDestroy
          initialValues={initValue}
          labelCol={{ span: 4 }}
          onFinish={(values) => { onCommit(values) }}>
          {dom}
        </Form>
      )}>
      <Form.Item name="id" label="id" hidden>
        <Input hidden />
      </Form.Item>
      <Form.Item name="name" label="名称" rules={[{ required: true, message: '请输入名称' }]}>
        <Input placeholder="名称" />
      </Form.Item>
      <Form.Item name="code" label="编码" rules={[{ required: true, message: '请输入编码' }]}>
        <Input placeholder="编码" />
      </Form.Item>
      <Form.Item name="type" label="表达式类型" rules={[{ required: true, message: '请选择类型' }]}>
        <Select options={types} ></Select>
      </Form.Item>
      <Form.Item noStyle shouldUpdate={(prev, current) => prev.type !== current.type} rules={[{ required: true, message: '请输入表达式' }]}>
        {({ getFieldValue }) => {
          const type = getFieldValue('type');
          if (type === 'argument') {
            return <Form.Item label="参数名" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <SearchInput handleSearch={(val, callback) => { callback([{ value: 'mock', text: 'mock' }]) }}
                placeholder='请输入参数名'>
              </SearchInput>
            </Form.Item>
          }
          if (type === 'variable') {
            return <Form.Item label="变量名" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <SearchInput handleSearch={(val, callback) => { callback([{ value: 'mock', text: 'mock' }]) }}
                placeholder='请输入变量名'>
              </SearchInput>
            </Form.Item>
          }
          if (type === 'arith') {
            return <Form.Item label="算术表达式" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <ComposeExpressionInput operates={arith} valueOptions={arithValueOpts} />
            </Form.Item>
          }
          if (type === 'logic') {
            return <Form.Item label="逻辑表达式" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <ComposeExpressionInput operates={logic} valueOptions={logicValueOpts} />
            </Form.Item>
          }
          if (type === 'compare') {
            return <Form.Item label="比较表达式" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <ComposeExpressionInput operates={compare} valueOptions={compareValueOpts} />
            </Form.Item>
          }
          if (type === 'function') {
            return <Form.Item label="函数表达式" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <FunctionExpressionInput functions={functions ?? defaultFunctions} />
            </Form.Item>
          }
          if (type === 'string') {
            return <Form.Item label="字符" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <Input placeholder="请输入字符"></Input>
            </Form.Item>
          }
          if (type === 'number') {
            <Form.Item label="数值" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <Input placeholder="请输入数值"></Input>
            </Form.Item>
          }
          if (type === 'time') {
            return <Form.Item label="时间" name="expression" rules={[{ required: true, message: '请输入' }]}>
              <Input placeholder="请输入时间"></Input>
            </Form.Item>
          }
          return <Form.Item label="表达式" name="expression" rules={[{ required: true, message: '请输入' }]}>
            <Input placeholder="请选择表达式类型" disabled={true}></Input>
          </Form.Item>
        }}
      </Form.Item>
      <Form.Item name='description' label="说明" rules={[{ required: true, message: '请输入规则说明' }]}>
        <Input.TextArea rows={4}></Input.TextArea>
      </Form.Item>
    </Modal>
  </>)

}

const VariableExpressionModal = (props: ExpressionModalProps) => {
  const { open, title, initValue, types, onClose, onCommit } = props
  return (<ExpressionModal
    open={open}
    title={title}
    initValue={initValue}
    types={types ?? variableOpts}
    onClose={onClose}
    onCommit={onCommit}>
  </ExpressionModal>)
}

const ConditionExpressionModal = (props: ExpressionModalProps) => {
  const { open, title, initValue, types, onClose, onCommit } = props
  return (<ExpressionModal
    open={open}
    title={title}
    initValue={initValue}
    types={types ?? conditionOpts}
    onClose={onClose}
    onCommit={onCommit}>
  </ExpressionModal>)
}

const ActionExpressionModal = (props: ExpressionModalProps) => {
  const { open, title, initValue, types, onClose, onCommit } = props
  return (<ExpressionModal
    open={open}
    title={title}
    initValue={initValue}
    types={types ?? actionOpts}
    functions={actionFunctions}
    onClose={onClose}
    onCommit={onCommit}>
  </ExpressionModal>)
}

export { Types, ComposeData, FunctionData, ExpressionData, VariableExpressionModal, ConditionExpressionModal, ActionExpressionModal };
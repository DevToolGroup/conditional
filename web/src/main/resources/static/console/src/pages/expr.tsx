import { Button, Col, DatePicker, Form, Input, Modal, Row, Select, SelectProps, Space } from "antd";
import { useEffect, useState } from "react";

interface Option {
  value: string
  label: string
}
const all: Option[] = [
  { value: 'string', label: '字符串' },
  { value: 'number', label: '数值' },
  { value: 'time', label: '时间' },
  { value: 'argument', label: '参数' },
  { value: 'variable', label: '变量' },
  { value: 'function', label: '函数' },
  { value: 'compare', label: '比较' },
  { value: 'arith', label: '算术' },
]

const functionArgs: Option[] = [
  { value: 'string', label: '字符串' },
  { value: 'number', label: '数值' },
  { value: 'time', label: '时间' },
  { value: 'argument', label: '参数' },
  { value: 'variable', label: '变量' },
]

const conditionOptions: Option[] = [
  { value: 'argument', label: '参数' },
  { value: 'variable', label: '变量' },
  { value: 'compare', label: '比较' },
  { value: 'function', label: '函数' },
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

const functions: Option[] = [
  { value: 'IN', label: '包含' }

]

interface FunctionArgument {
  name: string;
  code: string;
  types: Option[];
}

const defaultParams: { [key: string]: FunctionArgument[] } = {
  IN: [
    {
      name: '包含参数',
      code: '',
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
      code: '',
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
  ]
}

interface Compose {
  op?: string
  left?: Expression
  right?: Expression
}

interface Arg {
  code: string
  value?: string
}

interface Expression {
  type?: string
  value?: string
  compose?: Compose
  args?: Arg[]
}

interface ExpressionFormData {
  name?: string
  code?: string
  type?: string
  expression?: Expression
}

interface ExpressionFormProps {
  open: boolean
  title: string
  value?: ExpressionFormData
  options?: Option[]

  onClose: () => void
  onCommit: (data: ExpressionFormData) => void
}

interface SearchInputProps {
  placeholder: string
  value: string | null | undefined
  onChange: (value: string) => void
  handleSearch: (value: string, callback: (data: { value: string; text: string }[]) => void) => void
  style?: React.CSSProperties
}

const SearchInput = (props: SearchInputProps) => {
  const { value, onChange, handleSearch, style, placeholder } = props;
  const [data, setData] = useState<SelectProps['options']>([]);

  const handleChange = (newValue: string) => {
    onChange(newValue);
  };

  return (
    <Select
      showSearch
      value={value}
      placeholder={placeholder}
      style={style}
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


const ValueInput = ({ value, inputOptions, onChange }:
  {
    value: Expression | undefined,
    inputOptions?: Option[],
    onChange: (value: Expression) => void
  }) => {

  let input = <Input value={value?.value} onBlur={(e) => console.log(e.target.value)} placeholder="请输入字符"></Input>
  if (value?.type === 'argument') {
    input = <SearchInput value={value?.value}
      handleSearch={(val, callback) => { callback([{ value: 'mock', text: 'mock' }]) }}
      placeholder="请输入参数名称"
      onChange={(val) => console.log(val)} />

  } else if (value?.type === 'variable') {
    input = <SearchInput value={value?.value}
      handleSearch={(val, callback) => { console.log() }}
      placeholder="请输入变量名称"
      onChange={(val) => console.log(val)} />

  } else if (value?.type === 'string') {
    input = <Input value={value?.value} onBlur={(e) => console.log(e.target.value)} placeholder="请输入字符"></Input>

  } else if (value?.type === 'number') {
    input = <Input value={value?.value} onBlur={(e) => console.log(e.target.value)} placeholder="请输入数值"></Input>

  } else if (value?.type === 'time') {
    input = <DatePicker showTime onChange={(val) => console.log(val)} />

  }

  return (<>
    <Row gutter={8}>
      <Col span={6}>
        <Select options={inputOptions ?? [
          { value: 'number', label: '数值' },
          { value: 'time', label: '时间' },
          { value: 'argument', label: '参数' },
          { value: 'variable', label: '变量' }
        ]}
          value={value?.type}
          defaultValue='number'
          onChange={(val) => onChange ? onChange({ ...value, type: val }) : console.error("expression input error")}>
        </Select>
      </Col>
      <Col span={18}>
        {input}
      </Col>
    </Row>
  </>)
}

const ComposeExpressionInput = ({ value, options, onChange }: {
  value: Compose | undefined,
  options: Option[],
  onChange: (value: Compose) => void | undefined
}) => {

  return (<>
    <Row align='middle' gutter={[16, 16]}>
      <Col span={4} style={{ textAlign: 'center' }}>
        左值
      </Col>
      <Col span={20}>
        <ValueInput value={value?.left} onChange={(val) => onChange({ ...value, left: val })}></ValueInput>
      </Col>
      <Col span={4} style={{ textAlign: 'center' }}>操作</Col>
      <Col span={20}>
        <Select options={options} value={value?.op} onChange={(val) => onChange({ ...value, op: val })}></Select>
      </Col>
      <Col span={4} style={{ textAlign: 'center' }}>右值</Col>
      <Col span={20}>
        <ValueInput value={value?.right} onChange={(val) => onChange({ ...value, right: val })}></ValueInput>
      </Col>
    </Row>
  </>);
}

const VariableExpressionInput = ({ value, placeholder, label, onChange, handleSearch }: {
  value: string | undefined,
  placeholder: string,
  label: string
  onChange: (value: string) => void | undefined,
  handleSearch: (value: string, callback: (data: { value: string; text: string }[]) => void) => void
}) => {

  return (<>
    <Row gutter={16} align='middle'>
      <Col span={4} style={{ textAlign: 'center' }}>{label}</Col>
      <Col span={20}>
        <SearchInput handleSearch={handleSearch}
          value={value}
          onChange={(val) => onChange(val)}
          placeholder={placeholder}>
        </SearchInput>
      </Col>
    </Row>
  </>)
}

const FunctionExpressionInput = ({ value, args, onChange }: {
  value: string | undefined
  args: Arg[] | undefined
  onChange: ({ value, args }: { value: string, args: Arg[] }) => void
}) => {


  const onChangeFunction = (val: string): void => {
    const changedExpressions: any[] = []
    const param = defaultParams[val]
    for (var arg of param) {
      changedExpressions.push({ code: arg.code })
    }
    onChange({ value: val, args: changedExpressions });
  }

  const argTypes = (paramCode: any): Option[] => {
    const result: Option[] = []
    if (value === undefined) {
      return result;
    }
    const param = defaultParams[value]
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
        <Select options={functions} value={value} onChange={(val) => onChangeFunction(val)}></Select>
      </Col>
      <Col span={24} >
        <Row align='middle'>
          {args ? <Col span={2} style={{ textAlign: 'end' }}>参数</Col> : <></>}
          {
            args ? <Col span={22}>
              <Row gutter={[16, 16]}>
                  {
                    args.map((arg) => {
                      return <>
                        <Col span={2}>{arg.code}</Col>
                        <Col span={22}>
                          <ValueInput inputOptions={argTypes(arg.code)} value={arg} onChange={(val) => console.log(val)}></ValueInput>
                        </Col>
                      </>
                    })
                  }
              </Row>
            </Col> : <></>
          }
        </Row>
      </Col>
    </Row>
  </>)
}

const StringExpressionInput = ({ label, value, onChange }: { label: string, value: string | undefined, onChange: (val: string) => void }) => {
  return (<>
    <Row gutter={16} align='middle'>
      <Col span={4} style={{ textAlign: 'center' }}>
        {label}
      </Col>
      <Col span={20}>
        <Input value={value} onBlur={(e) => onChange(e.target.value)} placeholder="请输入字符"></Input>
      </Col>
    </Row>
  </>)
}

interface ExpressionInputProps {
  id?: string
  type: string
  value?: Expression
  onChange?: (value: Expression) => void
}

const ExpressionInput = (props: ExpressionInputProps) => {
  const { type, value, onChange } = props

  if (onChange === undefined) {
    return <></>
  }

  if (type === 'argument') {
    return <VariableExpressionInput value={value?.value} label='参数名'
      placeholder="请输入参数名"
      handleSearch={(val, callback) => { callback([{ value: 'mock', text: 'mock' }]) }}
      onChange={(val) => onChange({ ...value, value: val })} />

  }
  if (type === 'variable') {
    return <VariableExpressionInput value={value?.value} label='变量名'
      placeholder="请输入变量名"
      handleSearch={(val, callback) => { callback([{ value: 'mock', text: 'mock' }]) }}
      onChange={(val) => onChange({ ...value, value: val })} />
  }
  if (type === 'arith') {
    return <ComposeExpressionInput
      options={arith}
      value={value?.compose}
      onChange={(val) => onChange({ ...value, type: 'arith', compose: val })} />
  }
  if (type === 'compare') {
    return <ComposeExpressionInput
      options={compare}
      value={value?.compose}
      onChange={(val) => onChange({ ...value, type: 'compare', compose: val })} />
  }
  if (type === 'function') {
    return <FunctionExpressionInput
      value={value?.value}
      args={value?.args}
      onChange={(val) => { console.log('function change: ', val); onChange({ ...value, type: 'function', value: val.value, args: val.args }) }} />
  }
  if (type === 'string') {
    return <StringExpressionInput
      label="字符"
      value={value?.value}
      onChange={(val) => onChange({ ...value, type: 'string' })} />
  }
  if (type === 'number') {
    return <StringExpressionInput
      label="数值"
      value={value?.value}
      onChange={(val) => onChange({ ...value, type: 'number' })} />
  }
  if (type === 'time') {
    return <StringExpressionInput
      label="数值"
      value={value?.value}
      onChange={(val) => onChange({ ...value, type: 'time' })} />
  }
  return <ComposeExpressionInput
    options={compare}
    value={value?.compose}
    onChange={(val) => onChange({ ...value, type: 'compare', compose: val })} />
}

const ExpressionModalForm = (props: ExpressionFormProps) => {
  const { open, title, value, options, onClose, onCommit } = props;
  const [form] = Form.useForm()

  useEffect(() => {
    form.setFieldsValue(value)
    return () => {
      form.resetFields()
    }
  }, [value])

  const onChangeType = (val: string) => {
    onCommit({ ...value, type: val })
  }

  return (<>
    <Modal forceRender destroyOnClose
      width={700} open={open} title={title}
      okText="提交" cancelText="取消"
      onCancel={() => onClose()}
      modalRender={(dom) => (
        <Form form={form} name="form_in_modal"
          clearOnDestroy
          labelCol={{ span: 4 }}
          onFinish={(values) => onCommit(values)}>
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
      <Form.Item name="type" label="类型" rules={[{ required: true, message: '请选择类型' }]}>
        <Select options={options} value='compare' onChange={(e) => onChangeType(e)}></Select>
      </Form.Item>
      <Form.Item name="expression" label="表达式" rules={[{ required: true, message: '请输入表达式' }]}>
        <ExpressionInput type={value?.type ?? 'compare'}></ExpressionInput>
      </Form.Item>
      <Form.Item name='description' label="说明" rules={[{ required: true, message: '请输入规则说明' }]}>
        <Input.TextArea rows={4}></Input.TextArea>
      </Form.Item>
    </Modal>
  </>)

}

export default function ConditionExpression() {
  const [data, setData] = useState<ExpressionFormData>({})
  const [modalExpression, setModalExpression] = useState({ open: false, title: '设计' })
  return (<>
    <Button onClick={(e) => setModalExpression({ open: true, title: '设计' })}>设计</Button>
    <ExpressionModalForm
      open={modalExpression.open}
      title={modalExpression.title}
      value={data}
      options={conditionOptions}
      onClose={() => setModalExpression({ open: false, title: '设计' })}
      onCommit={(v) => { console.log(v); setData(v) }}
    ></ExpressionModalForm>
  </>)
}
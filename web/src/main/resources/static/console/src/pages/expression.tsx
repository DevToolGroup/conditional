import { Button, Card, Col, Flex, Form, Input, InputNumber, Modal, Row, Select, SelectProps, Skeleton, Space, Table, Tag, Typography } from "antd";
import { useState } from "react";
import {
  CloseOutlined,
  EditOutlined
} from '@ant-design/icons';
import InFunctionComponent from "./function/in";


let timeout: ReturnType<typeof setTimeout> | null;
let currentValue: string;

interface Type {
  value: string
  label: string
}

const logic = [
  { value: '&&', label: '且' },
  { value: '||', label: '或' },
  { value: '!', label: '非' },
  { value: '(', label: '(' },
  { value: ')', label: ')' },
]

const types = [
  {
    value: 'number',
    label: '数值'
  },
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
  },
  {
    value: 'function',
    label: '函数'
  }
]

const operates = [
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

const functions = [
  {
    value: 'IN',
    arguments: [
      {
        name: '包含参数',
        default: 'string',
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
        name: '被包含参数',
        default: 'string',
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
  },
  {
    value: 'MAX',
    arguments: [
      {
        name: '数值',
        default: 'number',
        types: [
          {
            value: 'number',
            label: '数值'
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
        name: '数值',
        default: 'number',
        types: [
          {
            value: 'number',
            label: '数值'
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
  },
  {
    value: 'SUBS',
    arguments: [
      {
        name: '字符串',
        default: 'string',
        types: [
          {
            value: 'string',
            label: '字符串'
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
        name: '起始位置',
        default: 'string',
        types: [
          {
            value: 'string',
            label: '字符串'
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
]

interface Argument {
  name: string
  value?: Value
  default: string
  types: Type[]
}

interface Value {
  type?: string
  value?: string | null | undefined | 0
  arguments?: Argument[] | null
}

interface Op {
  value: string
  label: string
}

interface ConditionExpression {
  op?: Op;
  left?: Value
  right?: Value
}

interface Condition {
  id: number
  name: string
  logic: Op
  expression: ConditionExpression
  description: string
}

interface Action {
  value: Value;
}

interface ExpressionInputProps {
  id?: string;
  value?: ConditionExpression;
  onChange?: (value: ConditionExpression) => void;
}

const fetch = (value: string, callback: (data: { value: string; text: string }[]) => void) => {
  if (timeout) {
    clearTimeout(timeout);
    timeout = null;
  }
  currentValue = value;

  const fake = () => {
    // const str = qs.stringify({
    //   code: 'utf-8',
    //   q: value,
    // });
    // jsonp(`https://suggest.taobao.com/sug?${str}`)
    //   .then((response: any) => response.json())
    //   .then((d: any) => {
    //     if (currentValue === value) {
    //       const { result } = d;
    //       const data = result.map((item: any) => ({
    //         value: item[0],
    //         text: item[0],
    //       }));
    //       callback(data);
    //     }
    //   });
    callback([
      { value: 'cs', text: '接口mock' },
      { value: 'IN', text: '包含' },
    ])
  };
  if (value) {
    timeout = setTimeout(fake, 300);
  } else {
    callback([]);
  }
};

interface SearchInputProps {
  placeholder: string
  value: string | null | undefined
  setValue: (value: string) => void
  handleSearch: (value: string, callback: (data: { value: string; text: string }[]) => void) => void
  style?: React.CSSProperties
}

const SearchInput = (props: SearchInputProps) => {
  const { value, setValue, handleSearch, style, placeholder } = props;
  const [data, setData] = useState<SelectProps['options']>([]);

  const handleChange = (newValue: string) => {
    setValue(newValue);
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

interface FunctionInputProps {
  value: Value | undefined
  setValue: (value: Value) => void
}

const FunctionInput = (props: FunctionInputProps) => {
  const { value, setValue } = props;
  console.log("function input: ", value)
  return (<>
    <div>参数</div>
    {value?.arguments?.map(arg => {
      <Row>
        <Col>{arg.name}</Col>
        <Col><Select defaultValue={arg.default} options={arg.types}></Select></Col>
        <Col><ValueInput value={arg.value} onChange={(val) => { console.log(val) }}></ValueInput></Col>
      </Row>
    })}
  </>)
}

interface ExpressionValueInputProps {
  value: Value | undefined
  onChange: (value: Value) => void
}

const ValueInput = (props: ExpressionValueInputProps) => {
  const { value, onChange } = props;
  if (value?.type === 'number') {
    return (
      <InputNumber style={{ width: '100%' }} defaultValue={value.value ?? 0} onChange={(e) => onChange({ ...value, value: e })}></InputNumber>
    )
  }
  if (value?.type === 'string') {
    return (<>
      <Input value={value?.value ?? ''} onChange={(e) => onChange({ ...value, value: e.target.value })}></Input>
    </>)
  }
  if (value?.type === 'argument') {
    const intValue = typeof value.value === 'number' ? (value.value === 0 ? null : String(value.value)) : value.value;
    return (
      <SearchInput handleSearch={(val, callback) => fetch(val, callback)}
        value={intValue}
        setValue={(val) => onChange({ ...value, value: val })}
        placeholder="请输入参数名称">
      </SearchInput>
    )
  }
  if (value?.type === 'variable') {
    const intValue = typeof value.value === 'number' ? (value.value === 0 ? null : String(value.value)) : value.value;
    return (
      <SearchInput handleSearch={(val, callback) => fetch(val, callback)}
        value={intValue}
        setValue={(val) => onChange({ ...value, value: val })}
        placeholder="请输入变量名称">
      </SearchInput>
    )
  }
  const intValue = typeof value?.value === 'number' ? (value.value === 0 ? null : String(value.value)) : value?.value;
  const selectedFunction = functions.filter((func) => func.value === value?.value).pop();
  const argumentsFromFunc = selectedFunction ? selectedFunction.arguments : null;
  return (
    <>
      <SearchInput handleSearch={(val, callback) => fetch(val, callback)}
        value={intValue}
        setValue={(val) => onChange({ ...value, value: val, arguments: argumentsFromFunc })}
        placeholder="请输入函数">
      </SearchInput>
      <FunctionInput value={value} setValue={(val) => onChange({ ...value, value: val.value, arguments: val.arguments })}></FunctionInput>
    </>
  )

}

const ExpressionInput = (props: ExpressionInputProps) => {
  const { id, value, onChange } = props;

  const getLabel = (val: string) => {
    for (var item of operates) {
      if (item.value === val) {
        return item.label;
      }
    }
    return val;
  }

  const setLeftType = (data: string) => {
    onChange?.({ ...value, left: { ...value?.left, type: data } })
  }
  const setLeftValue = (data: string | 0 | null | undefined) => {
    onChange?.({ ...value, left: { ...value?.left, value: data } });
  }

  const setOperate = (data: string) => {
    onChange?.({ ...value, op: { ...value?.op, value: data, label: getLabel(data) } });
  }

  const setRightType = (data: string) => {
    onChange?.({ ...value, right: { ...value?.right, type: data } })
  }
  const setRightValue = (data: string | 0 | null | undefined) => {
    onChange?.({ ...value, right: { ...value?.right, value: data } })
  }

  return (<div style={{ paddingTop: '32px' }} id={id}>
    <Row gutter={[16, 12]} align='middle' justify={'center'}>
      <Col span={3}>
        <Typography>左值</Typography>
      </Col>
      <Col span={4}>
        <Select options={types} value={value?.left?.type} onChange={setLeftType}></Select>
      </Col>
      <Col span={15}>
        <ValueInput value={value?.left} onChange={(value) => setLeftValue(value.value)}></ValueInput>
      </Col>
      <Col span={3}>
        <Typography>操作</Typography>
      </Col>
      <Col span={4}>
        <Select options={operates} value={value?.op?.value} onChange={setOperate}></Select>
      </Col>
      <Col span={15}></Col>
      <Col span={3}>
        <Typography>右值</Typography>
      </Col>
      <Col span={4}>
        <Select options={types} value={value?.right?.type} onChange={setRightType}></Select>
      </Col>
      <Col span={15}>
        <ValueInput value={value?.right} onChange={(value) => setRightValue(value.value)}></ValueInput>
      </Col>
    </Row>
  </div>)
}

interface ConditionExpressionComponentProps {
  conditions: Condition[]
  save: (conditions: Condition[]) => void
}

const ConditionExpressionComponent = (props: ConditionExpressionComponentProps) => {
  const { conditions, save } = props;
  const [form] = Form.useForm();
  const [open, setOpen] = useState({ open: false, title: '添加条件' });
  const onOpen = () => {
    setOpen({ open: true, title: '添加条件' });
    form.resetFields()
  }

  const onClose = () => {
    setOpen({ open: false, title: '' })
    form.resetFields()
  }

  const onCreate = (condition: Condition) => {
    const min = Math.ceil(1);
    const max = Math.floor(100);
    const id = Math.floor(Math.random() * (max - min + 1)) + min;
    condition.id = id;
    condition.logic = { value: '&&', label: getLogicLabel('&&') }
    save([...conditions, condition]);
    onClose();
  }

  const onEdit = (condition: Condition) => {
    form.setFieldsValue({ ...condition })
    setOpen({ open: true, title: '编辑条件' });
    console.log("onEdit condition: ", condition)
  }

  const onDelete = (condition: Condition) => {
    const deletedConditions = [...conditions].filter(item => condition.id !== item.id);
    save(deletedConditions)
  }

  const onChangeLogic = (condition: Condition) => {
    const changedConditions = [...conditions]
    for (var item of changedConditions) {
      if (item.id === condition.id) {
        item.logic = condition.logic;
      }
    }
    save([...conditions]);
  }

  const getLogicLabel = (value: string) => {
    for (let val of logic) {
      if (val.value === value) {
        return val.label;
      }
    }
    return value;
  }

  const getTypeLabel = (value: string | undefined) => {
    for (var type of types) {
      if (value === type.value) {
        return type.label;
      }
    }
    return value;
  }

  return (<>
    <Flex vertical style={{ padding: '10px 0' }}>
      <Flex vertical gap={8} style={{ marginBottom: '32px' }}>
        {conditions.length > 0 ? conditions.map((condition, index) => {
          return (
            <Row key={condition.id} justify="space-between" style={{ backgroundColor: 'rgb(230 250 255)', padding: '8px 24px' }}>
              <Col flex={9}>
                <Row>
                  <Col span={2}>
                    {
                      index !== 0 ?
                        <Select variant="borderless" size="small"
                          options={logic}
                          value={condition.logic.value}
                          onChange={(val) => onChangeLogic({ ...condition, logic: { value: val, label: getLogicLabel(val) } })}
                        />
                        : <div></div>
                    }
                  </Col>
                  <Col span={3}>
                    <Tag color="red">{condition.name}</Tag>
                  </Col>
                  <Col offset={1}>
                    <Space size='small'>
                      <Tag color="#108ee9">({getTypeLabel(condition.expression.left?.type)}){condition.expression.left?.value}</Tag>
                      <Tag color="#87d068">{condition.expression.op?.label}</Tag>
                      <Tag color="#108ee9">({getTypeLabel(condition.expression.right?.type)}){condition.expression.right?.value}</Tag>
                    </Space>
                  </Col>
                </Row>
              </Col>
              <Col flex={1} style={{ textAlign: 'end' }}>
                <Space size='middle'>
                  <EditOutlined onClick={() => onEdit(condition)} />
                  <CloseOutlined onClick={() => onDelete(condition)} />
                </Space>
              </Col>
            </Row>
          )
        }) : <Skeleton></Skeleton>}
      </Flex>
      <Button type="dashed" onClick={() => onOpen()}>添加条件</Button>
    </Flex>
    <Modal
      forceRender
      width={700}
      open={open.open}
      title={open.title}
      okText="提交"
      cancelText="取消"
      okButtonProps={{ autoFocus: true, htmlType: 'submit' }}
      onCancel={() => onClose()}
      destroyOnClose
      modalRender={(dom) => (
        <Form form={form} name="form_in_modal"
          clearOnDestroy
          onFinish={(values) => onCreate(values)}>
          {dom}
        </Form>
      )}
    >
      <div style={{ height: '18px' }}></div>
      <Form.Item name="id" label="id" hidden>
        <Input hidden />
      </Form.Item>
      <Form.Item name="name" label="名称" rules={[{ required: true, message: '请输入名称' }]}>
        <Input placeholder="名称" />
      </Form.Item>
      <Form.Item name="expression" label="配置" rules={[{ required: true, message: '请输入配置' }]}>
        <ExpressionInput />
      </Form.Item>
      <Form.Item name='description' label="说明" rules={[{ required: true, message: '请输入规则说明' }]}>
        <Input.TextArea rows={4}></Input.TextArea>
      </Form.Item>
    </Modal>
  </>);
}


interface ConditionActionComponentProps {
  actions: Action[]
  save: (action: Action[]) => void
}

const ConditionActionComponent = (props: ConditionActionComponentProps) => {
  const onAddAction = () => {

  }

  return (<>
    <Button type="dashed" onClick={() => onAddAction()}>添加动作</Button>
  </>)
}

interface Variable {
  id: number;
}

interface ConditionVariableComponentProps {
  variables: Variable[]
  onChange: (variables: Variable[]) => void;
}

const ConditionVariableComponent = (props: ConditionVariableComponentProps) => {
  const { variables, onChange } = props;

  const [form] = Form.useForm();
  const [open, setOpen] = useState({ open: false, title: '添加算子' });

  const onOpen = () => {
    setOpen({ open: true, title: '添加算子' });
    form.resetFields()
  }

  const onEdit = (variable: Variable) => {
    form.setFieldsValue({ ...variable })
    setOpen({ open: true, title: '编辑算子' });
  }

  const onClose = () => {
    setOpen({ open: false, title: '' });
  }

  const onDelete = (id: number) => {
    const deletedVariables = [...variables].filter(i => i.id !== id)
    onChange(deletedVariables)
  }

  const onCreate = (variable: Variable) => {

  }

  const columns = [
    {
      title: '变量名',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '变量编码',
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: '变量表达式',
      dataIndex: 'expression',
      key: 'expression',
    },
    {
      title: '操作',
      key: 'operate',
      render: (record: any) => (
        <Space size={'small'}>
          <Button type='link' onClick={() => onEdit(record)}>编辑</Button>
          <Button type='link' onClick={() => onDelete(record.id)}>删除</Button>
        </Space>
      ),
    },
  ];
  return (
    <Flex vertical>
      <Table dataSource={variables} columns={columns}></Table>
      <Button type="dashed" onClick={() => onOpen()}>添加算子</Button>
      <Modal
        forceRender
        width={700}
        open={open.open}
        title={open.title}
        okText="提交"
        cancelText="取消"
        okButtonProps={{ autoFocus: true, htmlType: 'submit' }}
        onCancel={() => onClose()}
        destroyOnClose
        modalRender={(dom) => (
          <Form form={form} name="form_in_modal"
            clearOnDestroy
            onFinish={(values) => onCreate(values)}>
            {dom}
          </Form>
        )}
      >
        <div style={{ height: '18px' }}></div>
        <Form.Item name="id" label="id" hidden>
          <Input hidden />
        </Form.Item>
        <Form.Item name="name" label="名称" rules={[{ required: true, message: '请输入名称' }]}>
          <Input placeholder="名称" />
        </Form.Item>
        <Form.Item name="code" label="编码" rules={[{ required: true, message: '请输入编码' }]}>
          <Input placeholder="编码" />
        </Form.Item>
        <Form.Item name="expression" label="配置" rules={[{ required: true, message: '请输入配置' }]}>
          <ExpressionInput />
        </Form.Item>
        <Form.Item name='description' label="说明" rules={[{ required: true, message: '请输入规则说明' }]}>
          <Input.TextArea rows={4}></Input.TextArea>
        </Form.Item>
      </Modal>
    </Flex>
  )
}

export default function RuleComponent() {
  const [conditions, setConditions] = useState<Condition[]>([])
  const [actions, setActions] = useState<Action[]>([])
  const [variables, setVariables] = useState<Variable[]>([])

  return (
    <Flex vertical style={{ padding: '32px' }}>
      <Flex vertical gap={12}>
        <Card title='变量'>
          <Flex vertical>
            <ConditionVariableComponent variables={variables} onChange={(data) => { setVariables(data) }}></ConditionVariableComponent>
          </Flex>
        </Card>
        <Card title='条件'>
          <Flex vertical>
            <ConditionExpressionComponent conditions={conditions} save={(conditions) => { setConditions(conditions) }}></ConditionExpressionComponent>
          </Flex>
        </Card>
        <Card title='动作'>
          <Flex vertical justify="flex-end">
            <ConditionActionComponent actions={actions} save={(actions) => setActions(actions)}></ConditionActionComponent>
          </Flex>
        </Card>
      </Flex>
    </Flex>
  )
}
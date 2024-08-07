import { Button, Card, Col, Flex, Row, Select, Skeleton, Space, Table, Tag } from "antd";
import { lazy, useState } from "react";
import {
  Types, ComposeData, ExpressionData,
  VariableExpressionModal, ConditionExpressionModal, ActionExpressionModal,
  FunctionData
} from "./expression";
import {
  EditOutlined,
  CloseOutlined
} from '@ant-design/icons';


interface Op {
  value: string
  label: string
}

interface Condition {
  code: string
  logic: Op
  expression: ExpressionData
}

const logic = [
  { value: "&&", label: "且" },
  { value: "||", label: "或" },
  { value: "!", label: "非" },
  { value: "(", label: "左嵌套" },
  { value: ")", label: "右嵌套" },
]

const and = { value: "&&", label: "且" }


const ConditionActionComponent = () => {
  const [actions, setActions] = useState<ExpressionData[]>([])
  const [actionModal, setActionModal] = useState<{ state: boolean, title: string, initValue?: ExpressionData }>({ state: false, title: '' });

  const onCommit = (val: ExpressionData) => {
    setActions([...actions, val])
    setActionModal({ state: false, title: '' })
  }

  return (<>
    <Flex vertical style={{ padding: '10px 0' }}>
      <Flex vertical gap={8} style={{ marginBottom: '32px' }}>
        {actions.length > 0 ? actions.map((action, index) => {
          return (
            <Row key={action.code} justify="space-between" style={{ backgroundColor: 'rgb(230 250 255)', padding: '8px 24px' }}>
              <Col flex={9}>
                <Row>
                  <Col span={3}>
                    <Tag color="red">{action.name}</Tag>
                  </Col>
                </Row>
              </Col>
              <Col flex={1} style={{ textAlign: 'end' }}>
                <Space size='middle'>
                  <EditOutlined onClick={() => setActionModal({ state: true, title: '编辑动作', initValue: action })} />
                  <CloseOutlined onClick={() => setActions(actions.filter(item => action.code !== item.code))} />
                </Space>
              </Col>
            </Row>
          )
        }) : <Skeleton></Skeleton>}
      </Flex>
      <Button type="dashed" onClick={() => setActionModal({ state: true, title: '创建动作' })}>添加动作</Button>
    </Flex>
    <ActionExpressionModal
      open={actionModal.state}
      title={actionModal.title}
      initValue={actionModal.initValue}
      onClose={() => setActionModal({ state: false, title: '' })}
      onCommit={(value) => onCommit(value)}
    >
    </ActionExpressionModal>
  </>)
}

const ConditionExpressionComponent = () => {
  const [conditions, setConditions] = useState<Condition[]>([])
  const [conditionModal, setConditionModal] = useState<{ state: boolean, title: string, initValue?: ExpressionData }>({ state: false, title: '' });

  const onChangeLogic = (condition: Condition) => {
    const changedConditions = [...conditions]
    for (var item of changedConditions) {
      if (item.code === condition.code) {
        item.logic = condition.logic;
      }
    }
    setConditions([...conditions]);
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
    for (var type of Types) {
      if (value === type.value) {
        return type.label;
      }
    }
    return value;
  }

  const onCommit = (val: ExpressionData) => {
    setConditions([...conditions, { expression: val, logic: and, code: val.code }])
    setConditionModal({ state: false, title: '' })
  }

  const getComponent = (condition: Condition) => {
    if (condition.expression.type === 'compare') {
      return (<Space size='small'>
        <Tag color="#108ee9">({getTypeLabel((condition.expression as ComposeData).left?.type)}){(condition.expression as ComposeData).left?.value}</Tag>
        <Tag color="#87d068">{(condition.expression as ComposeData).op}</Tag>
        <Tag color="#108ee9">({getTypeLabel((condition.expression as ComposeData).right?.type)}){(condition.expression as ComposeData).right?.value}</Tag>
      </Space>)
    }
    if (condition.expression.type === 'variable' || condition.expression.type === 'argument') {
      return (<Space size='small'>
        <Tag color="#108ee9">({getTypeLabel(condition.expression.type)}){condition.expression.expression as string}</Tag>
      </Space>)
    }
  }

  return (<>
    <Flex vertical style={{ padding: '10px 0' }}>
      <Flex vertical gap={8} style={{ marginBottom: '32px' }}>
        {conditions.length > 0 ? conditions.map((condition, index) => {
          return (
            <Row key={condition.code} justify="space-between" style={{ backgroundColor: 'rgb(230 250 255)', padding: '8px 24px' }}>
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
                    <Tag color="red">{condition.expression.name}</Tag>
                  </Col>
                  <Col span={18}>
                    {getComponent(condition)}
                  </Col>
                </Row>
              </Col>
              <Col flex={1} style={{ textAlign: 'end' }}>
                <Space size='middle'>
                  <EditOutlined onClick={() => setConditionModal({ state: true, title: '编辑条件', initValue: condition.expression })} />
                  <CloseOutlined onClick={() => setConditions(conditions.filter(item => condition.code !== item.code))} />
                </Space>
              </Col>
            </Row>
          )
        }) : <Skeleton></Skeleton>}
      </Flex>
      <Button type="dashed" onClick={() => setConditionModal({ state: true, title: '创建条件' })}>添加条件</Button>
    </Flex>
    <ConditionExpressionModal
      open={conditionModal.state}
      title={conditionModal.title}
      initValue={conditionModal.initValue}
      onClose={() => setConditionModal({ state: false, title: '' })}
      onCommit={(value) => onCommit(value)}
    >
    </ConditionExpressionModal>
  </>)
}

const ConditionVariableComponent = () => {
  const [records, setRecords] = useState<ExpressionData[]>([])
  const [loading, setLoading] = useState(false);
  const [variableModal, setVariableModal] = useState<{ state: boolean, title: string, initValue?: ExpressionData }>({ state: false, title: '' });
  const onCommit = (val: ExpressionData) => {
    setRecords([...records, val])
    setVariableModal({ state: false, title: '' })
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
          <Button type='link' onClick={() => setVariableModal({ state: true, title: '编辑变量', initValue: record })}>编辑</Button>
          <Button type='link' onClick={() => setRecords(records.filter((i) => i.code !== record.code))}>删除</Button>
        </Space>
      ),
    },
  ];
  return (
    <Flex vertical>
      <Table pagination={false} size="small" dataSource={records} columns={columns}></Table>
      <Button type="dashed" onClick={() => setVariableModal({ state: true, title: '创建变量' })}>添加变量</Button>
      <VariableExpressionModal
        onClose={() => setVariableModal({ state: false, title: '' })}
        onCommit={onCommit}
        initValue={variableModal.initValue}
        open={variableModal.state}
        title={variableModal.title} >
      </VariableExpressionModal>
    </Flex>
  )
}
const ConditionClass = () => {
  return (
    <>
      <Flex vertical style={{ padding: '32px' }}>
        <Flex vertical gap={12}>
          <Card title='变量'>
            <ConditionVariableComponent></ConditionVariableComponent>
          </Card>
          <Card title='条件'>
            <ConditionExpressionComponent></ConditionExpressionComponent>
          </Card>
          <Card title='动作'>
            <ConditionActionComponent></ConditionActionComponent>
          </Card>
        </Flex>
      </Flex>
    </>
  );
}

export default ConditionClass
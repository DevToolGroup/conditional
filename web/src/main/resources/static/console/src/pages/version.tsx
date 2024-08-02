import { Button, Col, Drawer, Flex, Form, Input, Modal, Row, Select, Space, Table, TableProps, Tag, theme } from 'antd';
import { useState } from 'react';
import { useMatch, useSearchParams, history } from 'umi';

interface DataType {
  id: number;
  key: number;
  version: string;
  description: string;
  createTime: string;
}

const data: DataType[] = [
  {
    id: 1,
    key: 1,
    version: '1',
    description: 'New York No. 1 Lake Park',
    createTime: '2023-01-12'
  },
  {
    id: 2,
    key: 2,
    version: '2',
    description: 'London No. 1 Lake Park',
    createTime: '2023-01-12'

  },
  {
    id: 3,
    key: 3,
    version: '3',
    description: 'Sydney No. 1 Lake Park',
    createTime: '2023-01-12'
  },
];

interface TestProps {
  open?: boolean;
  onClose?: (e: React.MouseEvent | React.KeyboardEvent) => void;
  record: null | DataType
}

interface TestData {
  id?: number;
  code: string;
  argument: string;
  result: string;
}

function Test({ open = false, onClose, record }: TestProps) {
  const [form] = Form.useForm();

  const onTest = (values: TestData) => {
    values.id = record?.id;
  }

  return (<>
    <Drawer
      title='规则验证'
      width={640}
      onClose={onClose}
      open={open}
      styles={{
        body: {
          paddingBottom: 80,
        },
      }}
      extra={
        <Space>
          <Button onClick={onClose}>取消</Button>
        </Space>
      }
    >
      <Form layout="vertical" form={form} clearOnDestroy onFinish={(values) => onTest(values)}>
        <Row gutter={16}>
          <Col span={24}>
            <Form.Item name="code" label="规则编码" rules={[{ required: true, message: '请输入规则编码' }]}>
              <Input placeholder="请输入规则编码" />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={24}>
            <Form.Item name="argument" label="验证参数" rules={[{ required: true, message: '请输入验证参数' }]}>
              <Input.TextArea rows={4} placeholder="请输入规则表述" />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={24}>
            <Form.Item name="result" label="验证结果">
              <Input.TextArea rows={10} placeholder="等待验证结果" disabled />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Drawer>
  </>)
}

export default function RuleVersionPage() {
  const [form] = Form.useForm();
  const [record, setRecords] = useState(data)
  const [open, setOpen] = useState(false)
  const [test, setTest] = useState<{ open: boolean, record: null | DataType }>({ open: false, record: null })
  const [title, setTitle] = useState('创建版本')

  const match = useMatch('/rule/:ruleId/version')
  const [params] = useSearchParams()

  const onCreate = (data: DataType) => {
    console.log("receive: ", data);
    let createRecords = [...record];

    if (data.id === undefined) {
      const min = Math.ceil(1);
      const max = Math.floor(100);
      const id = Math.floor(Math.random() * (max - min + 1)) + min;
      data.id = id;
      data.key = id;
      createRecords.push(data);
    } else {
      createRecords = createRecords.map((record) => {
        if (record.id === data.id) {
          return { ...record, ...data }
        }
        return record;
      })
    }
    console.log()
    setRecords(createRecords);
    setOpen(false);
  }

  const onEdit = (record: DataType) => {
    setTitle("编辑规则")
    form.setFieldsValue({
      ...record,
    });
    setOpen(true);
  }

  const onTest = (record: DataType) => {
    setTest({ open: true, record: record });
  }

  const onDelete = (id: number) => {
    // 从data删除对应id的数据
    setRecords(data => data.filter(item => item.id !== id));
  }

  const onDeploy = (recordId: number) => {

  }

  const onUnDeploy = (recordId: number) => {

  }

  function toDesign(record: DataType): void {
    const url = '/rule/' + match?.params.ruleId + '/version/' + record.id + '/design/factClass?rule=' + params.get("rule");
    history.push(url, record);
  }

  const columns: TableProps<DataType>['columns'] = [
    {
      title: '序号',
      dataIndex: 'id',
      key: 'id',
      render: (text) => <a>{text}</a>,
    },
    {
      title: '版本号',
      dataIndex: 'version',
      key: 'version',
    },
    {
      title: '版本说明',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      key: 'createTime',
    },
    {
      title: '操作',
      key: 'action',
      width: '100px',
      render: (_, record) => (
        <Space size="small">
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => toDesign(record)}>设计</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onEdit(record)}>编辑</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onTest(record)}>验证</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onDeploy(record.id)}>部署</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onUnDeploy(record.id)}>下线</Button>
          <Button style={{ padding: '0 2px' }} type='link' color='danger' onClick={() => onDelete(record.id)}>删除</Button>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Flex gap={16} style={{ marginBottom: '15px' }}>
        <Button type='primary' onClick={() => setOpen(true)}>创建</Button>
      </Flex>
      <Table columns={columns} dataSource={record} />
      <Modal
        forceRender
        open={open}
        title={title}
        okText="提交"
        cancelText="取消"
        okButtonProps={{ autoFocus: true, htmlType: 'submit' }}
        onCancel={() => setOpen(false)}
        destroyOnClose
        modalRender={(dom) => (
          <Form layout="vertical" form={form} name="form_in_modal"
            initialValues={{ modifier: 'public' }} clearOnDestroy
            onFinish={(values) => onCreate(values)}>
            {dom}
          </Form>
        )}
      >
        <Form.Item name="id" label="id" hidden>
          <Input hidden />
        </Form.Item>
        <Form.Item name="description" label="版本说明" rules={[{ required: true, message: '请输入版本说明' }]}>
          <Input.TextArea rows={4} placeholder="请输入版本说明" />
        </Form.Item>
      </Modal>
      <Test open={test.open} record={test.record} onClose={() => setTest({ open: false, record: null })}></Test>
    </>
  );
};


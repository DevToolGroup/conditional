import { Button, Flex, Form, Input, Modal, Select, Space, Table, TableProps } from 'antd';
import { useState } from 'react';

interface DataType {
  id: number;
  key: number;
  code: string;
  type: string;
  typeName: string;
}

const data: DataType[] = [
  {
    id: 1,
    key: 1,
    code: 'John Brown',
    type: 'New York No. 1 Lake Park',
    typeName: '纽约',
  },
  {
    id: 2,
    key: 2,
    code: 'Jim Green',
    type: 'London No. 1 Lake Park',
    typeName: 'London No. 1 Lake Park',
  },
  {
    id: 3,
    key: 3,
    code: 'Joe Black',
    type: 'Sydney No. 1 Lake Park',
    typeName: 'Sydney No. 1 Lake Park',
  },
];


export default function ArgumentPage() {
  const [form] = Form.useForm();
  const [open, setOpen] = useState(false);
  const [title, setTitle] = useState("创建参数");
  const [record, setRecords] = useState(data);

  const onOpen = () => {
    form.resetFields();
    setOpen(true);
  }

  const onEdit = (record: DataType) => {
    setTitle("编辑事实")
    form.setFieldsValue({
      ...record,
    });
    setOpen(true);
  }

  const onCreate = (data: DataType) => {
    console.log('receive data: ', data);
    let createRecords = [...record];

    if (data.id === undefined) {
      const min = Math.ceil(1);
      const max = Math.floor(20);
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
    setRecords(createRecords);
    setOpen(false);
  }

  const onDelete = (id: number) => {
    // 从data删除对应id的数据
    setRecords(data => data.filter(item => item.id !== id));
  }

  const columns: TableProps<DataType>['columns'] = [
    {
      title: '序号',
      dataIndex: 'id',
      key: 'id',
      align: 'center'
    },
    {
      title: '参数编码',
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: '参数类型',
      dataIndex: 'type',
      key: 'type',
      render: (_, record) => {
        return (record.typeName);
      }
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => onEdit(record)}>编辑</Button>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => onDelete(record.id)}>删除</Button>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Flex gap={24} justify='flex-end' style={{ marginBottom: '13px' }}>
        <Button type="primary" onClick={onOpen}>新增参数</Button>
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
            clearOnDestroy
            onFinish={(values) => onCreate(values)}>
            {dom}
          </Form>
        )}
      >
        <Form.Item name="id" label="id" hidden>
          <Input hidden />
        </Form.Item>
        <Form.Item name="code" label="参数编码" rules={[{ required: true, message: '请输入参数编码' }]}>
          <Input placeholder="参数编码" />
        </Form.Item>
        <Form.Item name="type" label="参数类型"
          rules={[
            {
              required: true,
              message: '请输入参数类型',
            },
          ]}>
          <Select placeholder="请输入参数类型" options={[
            { value: 'User', label: '用户' },
            { value: 'Order', label: '订单' },
          ]}>
          </Select>
        </Form.Item>
      </Modal>
    </>
  );
};


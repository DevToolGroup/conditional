import { Button, Col, Drawer, Flex, Form, Input, Modal, Row, Select, Space, Table, TableProps, Tag, theme } from 'antd';
import { history } from 'umi';
import Search from 'antd/lib/input/Search';
import { useState } from 'react';

interface DataType {
  id: number;
  key: number;
  code: string;
  name: string;
  description: string;
}

const data: DataType[] = [
  {
    id: 1,
    key: 1,
    code: 'John Brown',
    name: 'John',
    description: 'New York No. 1 Lake Park',
  },
  {
    id: 2,
    key: 2,
    code: 'Jim Green',
    name: 'Jim',
    description: 'London No. 1 Lake Park',
  },
  {
    id: 3,
    key: 3,
    code: 'Joe Black',
    name: 'Joe',
    description: 'Sydney No. 1 Lake Park',
  },
];

export default function RulePage() {
  const [form] = Form.useForm();
  const [record, setRecords] = useState(data);
  const [open, setOpen] = useState(false);
  const [title, setTitle] = useState("创建版本");

  const columns: TableProps<DataType>['columns'] = [
    {
      title: '序号',
      dataIndex: 'id',
      key: 'id',
      align: 'center'
    },
    {
      title: '规则编码',
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: '规则名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '规则描述',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: '操作',
      key: 'action',
      width: '100px',
      render: (_, record) => (
        <Space size='small'>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => history.push(buildVersionUrl(record))}>规则版本</Button>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => onEdit(record)}>编辑</Button>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => onDelete(record.id)}>删除</Button>
        </Space>
      ),
    },
  ];

  const onCreate = (data: DataType) => {
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
    setRecords(createRecords);
    setOpen(false);
  };

  const onSearch = () => {

  }

  const onEdit = (record: DataType) => {
    setTitle("编辑规则")
    form.setFieldsValue({
      ...record,
    });
    setOpen(true);
  }
  const onDelete = (id: number) => {
    // 从data删除对应id的数据
    setRecords(data => data.filter(item => item.id !== id));
  }

  const buildVersionUrl = (record: DataType) => {
    return '/rule/' + record.id + '/version?rule=' + record.name;
  }

  return (
    <>
      <Flex gap={16} style={{ marginBottom: '15px' }}>
        <Button type='primary' onClick={() => setOpen(true)}>创建</Button>
        <Search placeholder="输入编码" onSearch={onSearch} style={{ maxWidth: '38%' }} />
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
        <Form.Item name="code" label="规则编码" rules={[{ required: true, message: '请输入规则编码' }]}>
          <Input placeholder="请输入规则编码" />
        </Form.Item>
        <Form.Item name="description" label="描述" rules={[{ required: true, message: '请输入规则表述' }]}>
          <Input.TextArea rows={4} placeholder="请输入规则表述" />
        </Form.Item>
      </Modal>
    </>
  );
};


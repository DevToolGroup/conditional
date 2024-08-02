import { Button, Col, Drawer, Flex, Form, Input, Row, Select, Space, Table, TableProps, Tag, theme } from 'antd';
import Search from 'antd/lib/input/Search';
import { useState } from 'react';

const { Option } = Select;

interface DataType {
  id: number;
  key: number;
  code: string;
  type: string;
}

const data: DataType[] = [
  {
    id: 1,
    key: 1,
    code: 'John Brown',
    type: 'New York No. 1 Lake Park',
  },
  {
    id: 2,
    key: 2,
    code: 'Jim Green',
    type: 'London No. 1 Lake Park',
  },
  {
    id: 3,
    key: 3,
    code: 'Joe Black',
    type: 'Sydney No. 1 Lake Park',
  },
];


export default function ArgumentPage() {

  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const [open, setOpen] = useState(false);

  const [drawerTitle, setDrawerTitle] = useState("创建版本");

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
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button style={{padding: '0 4px'}} type='link' onClick={onEdit}>编辑</Button>
          <Button style={{padding: '0 4px'}} type='link' onClick={onDelete}>删除</Button>
        </Space>
      ),
    },
  ];

  const onEdit = () => {
    setDrawerTitle("编辑输入")
  }
  const onDelete = () => {

  }

  const showDrawer = () => {
    setOpen(true);
  };

  const onClose = () => {
    setOpen(false);
  };

  const onSubmit = () => {
    setOpen(false);
  };

  const onSearch = () => {

  }

  return (
    <>
      <Flex gap={24} justify='flex-end' style={{marginBottom: '13px'}}>
        <Button type="primary" onClick={showDrawer}>新增输入参数</Button>
      </Flex>
      <Table columns={columns} dataSource={data} />
      <Drawer
        title={drawerTitle}
        width={480}
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
            <Button onClick={onSubmit} type="primary">确认</Button>
          </Space>
        }
      >
        <Form layout="vertical">
          <Row gutter={16}>
            <Form.Item name="id" label="id" hidden>
              <Input hidden />
            </Form.Item>
          </Row>
          <Row gutter={16}>
            <Col span={24}>
              <Form.Item name="code" label="参数编码" rules={[{ required: true, message: '请输入参数编码' }]}>
                <Input placeholder="参数编码" />
              </Form.Item>
            </Col>

          </Row>
          <Row gutter={16}>
            <Col span={24}>
              <Form.Item name="type" label="参数类型"
                rules={[
                  {
                    required: true,
                    message: '请输入参数类型',
                  },
                ]}>
                <Input placeholder="请输入参数类型" />
              </Form.Item>
            </Col>
          </Row>
        </Form>
      </Drawer>
    </>
  );
};


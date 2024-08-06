import { EditableProTable, ProCard, ProColumns, ProFormField } from "@ant-design/pro-components";
import { Button, Col, Drawer, Flex, Form, GetRef, Input, InputRef, Modal, Popconfirm, Row, Space, Table, TableProps, Typography } from "antd";
import React, { useContext, useEffect, useRef } from "react";
import { useState } from 'react';

interface DataType {
  id: number;
  key: number;
  code: string;
  name: string;
}

const TypeEnum = {
  string: {
    text: '字符串'
  },
  Boolean: {
    text: '布尔值',
  },
  Integer: {
    text: '整数',
  },
  Long: {
    text: '长整数',
  },
  Float: {
    text: '单精度浮点数',
  },
  Double: {
    text: '双精度浮点数',
  },
  Decimal: {
    text: '任意精度十进制数',
  },
  Time: {
    text: '时间',
  },
  List: {
    text: '列表',
  },
  Map: {
    text: '字典',
  }
}

const data: DataType[] = [
  {
    id: 1,
    key: 1,
    code: "User",
    name: "用户",
  },
  {
    id: 2,
    key: 2,
    code: "Order",
    name: "订单",
  },
  {
    id: 3,
    key: 3,
    code: "Score",
    name: '积分',
  },
];


interface PropertyType {
  id: React.Key;
  code?: string;
  name?: string;
  type?: string;
  keyType?: string;
  valueType?: string;
}


const properties: PropertyType[] = [
  {
    id: 0,
    code: 'name',
    name: '名称',
    type: 'String',
  },
  {
    id: 1,
    code: 'scores',
    name: '历史积分',
    type: 'List',
    valueType: 'Score',
  },
]


interface PropertyProps {
  record: DataType | null
}

const waitTime = (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

const Property = ({ record }: PropertyProps) => {
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);
  const [dataSource, setDataSource] = useState<readonly PropertyType[]>([]);

  const columns: ProColumns<PropertyType>[] = [
    {
      title: '序号',
      width: '5%',
      dataIndex: 'id',
      readonly: true,
    },
    {
      title: '编码',
      dataIndex: 'code',
    },
    {
      title: '名称',
      dataIndex: 'name',
    },
    {
      title: '类型',
      dataIndex: 'type',
      valueType: 'select',
      valueEnum: TypeEnum
    },
    {
      title: '键类型',
      dataIndex: 'keyType',
      valueType: 'select',
      valueEnum: TypeEnum
    },
    {
      title: '值类型',
      dataIndex: 'valueType',
      valueType: 'select',
      valueEnum: TypeEnum
    },
    {
      title: '操作',
      width: '30%',
      valueType: 'option',
      dataIndex: 'operation',
      render: (text, record, _, action) => [
        <a key="editable" onClick={() => { action?.startEditable?.(record.id) }}>
          编辑
        </a>,
        <a key="delete" onClick={() => { setDataSource(dataSource.filter((item) => item.id !== record.id)) }}>
          删除
        </a>,
      ],
    },
  ];



  return (
    <>
      <div>
        <Flex justify="space-between" align="center">
          <div style={{ fontSize: 16, fontWeight: 600, margin: '16px 0' }}>
            【{record?.name}】相关属性
          </div>
        </Flex>
      </div>
      <EditableProTable<PropertyType>
        rowKey="id"
        headerTitle={false}
        maxLength={5}
        scroll={{
          x: 960,
        }}
        loading={false}
        columns={columns}
        request={async () => ({
          data: properties,
          total: 3,
          success: true,
        })}
        recordCreatorProps={
          {
            record: () => ({ id: (Math.random() * 1000000).toFixed(0) }),
          }
        }
        value={dataSource}
        onChange={(v) => { setDataSource(v); console.log("v")}}
        editable={{
          type: 'multiple',
          editableKeys,
          onSave: async (rowKey, data, row) => {
            console.log(rowKey, data, row);
            await waitTime(2000);
          },
          onChange: setEditableRowKeys,
        }}
      />
    </>
  )
}

export default function FactPage() {
  const [form] = Form.useForm();
  const [record, setRecords] = useState(data);
  const [open, setOpen] = useState(false);
  const [openProperty, setOpenProperty] = useState<{ open: boolean, record: null | DataType }>({ open: false, record: null });
  const [title, setTitle] = useState("创建事实");

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

  const onDelete = (id: number) => {
    // 从data删除对应id的数据
    setRecords(data => data.filter(item => item.id !== id));
  }

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
  }

  const columns: TableProps<DataType>['columns'] = [
    {
      title: '序号',
      width: '5%',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: '编码',
      width: '15%',
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: '名称',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '操作',
      key: 'action',
      width: '30%',
      render: (_, record) => (
        <Space size="middle">
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => setOpenProperty({ open: true, record: record })}>属性</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onEdit(record)}>编辑</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onDelete(record.id)}>删除</Button>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Flex justify='flex-end' style={{ marginBottom: '13px' }}>
        <Button type="primary" onClick={() => onOpen()}>新增事实</Button>
      </Flex>
      <Space size={'small'}></Space>
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
        <Form.Item name="code" label="事实编码" rules={[{ required: true, message: '请输入事实编码' }]}>
          <Input.TextArea rows={4} placeholder="请输入事实编码" />
        </Form.Item>
        <Form.Item name="name" label="事实名称" rules={[{ required: true, message: '请输入事实名称' }]}>
          <Input.TextArea rows={4} placeholder="请输入事实名称" />
        </Form.Item>
      </Modal>
      <Drawer title='属性' width={1200} onClose={() => setOpenProperty({ open: false, record: null })} open={openProperty.open}
        styles={{ body: { paddingBottom: 80 } }}>
        <Property record={openProperty.record}></Property>
      </Drawer>
    </>
  );
}

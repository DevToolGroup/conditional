import { Button, Flex, Space, Table, TableProps } from 'antd';
import { useState } from 'react';
import { ExpressionData, VariableExpressionModal } from './expression';

const data: ExpressionData[] = [
  {
    code: 'John Brown',
    name: 'New York No. 1 Lake Park',
  },
  {
    code: 'Jim Green',
    name: 'London No. 1 Lake Park',
  },
  {
    code: 'Joe Black',
    name: 'Sydney No. 1 Lake Park',
  },
];

export default function VariablePage() {
  const [variableModal, setVariableModal] = useState<{ state: boolean, title: string, initValue?: ExpressionData }>({ state: false, title: '' });
  const [record, setRecords] = useState<ExpressionData[]>(data);
  const onCommit = (data: ExpressionData) => {
    const exists = record.filter(i => i.code === data.code)
    if (exists === undefined || exists.length === 0) {
      record.push(data);
    } else {
      exists.forEach(i => {i.code = data.code})
    }
  };

  const onDelete = (code: string) => {
    // 从data删除对应id的数据
    setRecords(data => data.filter(item => item.code !== code));
  }

  const columns: TableProps<ExpressionData>['columns'] = [
    {
      title: '序号',
      dataIndex: 'id',
      key: 'id',
      align: 'center'
    },
    {
      title: '变量编码',
      dataIndex: 'code',
      key: 'code',
    },
    {
      title: '变量名',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => setVariableModal({ state: true, title: '编辑变量', initValue: record })}>编辑</Button>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => onDelete(record.code)}>删除</Button>
        </Space>
      ),
    },
  ];

  return (
    <>
      <Flex justify='flex-end' style={{ marginBottom: '13px' }}>
        <Button type="primary" onClick={() => setVariableModal({ state: true, title: '创建变量' })}>新增变量</Button>
      </Flex>
      <Table columns={columns} dataSource={data} />
      <VariableExpressionModal
        onClose={() => setVariableModal({ state: false, title: '' })}
        onCommit={onCommit}
        initValue={variableModal.initValue}
        open={variableModal.state}
        title={variableModal.title} >
      </VariableExpressionModal>

    </>
  );
};


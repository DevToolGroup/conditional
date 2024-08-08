import { Button, message, Space } from 'antd';
import { history } from '@umijs/max';
import { useRef, useState } from 'react';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import RuleModal from './modal';
import services from '@/services/rule';

const { addRule, deleteRule, updateRule, queryRule } =
  services.RuleApi;

export default function RulePage() {
  const [modal, setModal] = useState<{ scene: string, open: boolean, title?: string, initValue?: RULE.Rule }>({ scene: 'create', open: false, title: '创建规则' });
  const ref = useRef<ActionType>();
  
  const onCreate = () => {
    setModal({ scene: 'create', open: true, title: '创建规则' })
  }

  const onCreateSave = async (val: RULE.Rule) => {
    try {
      await addRule({ ...val });
      message.success('添加成功');
      setModal({ scene: 'create', open: false })
      ref.current?.reload();
    } catch (error) {
      message.error('添加失败请重试！');
      return false;
    }
  }

  const onUpdate = (record: RULE.Rule) => {
    setModal({ scene: 'update', open: true, title: '编辑规则', initValue: record });
  }

  const onUpdateSave = async (record: RULE.Rule) => {
    try {
      await updateRule({ id: record.id }, { ...record });
      ref.current?.reload();
      message.success('更新成功');
      setModal({ scene: 'update', open: false })
    } catch (error) {
      message.error('更新失败请重试！');
      return false;
    }
  }

  const onCancel = () => {
    setModal({ scene: '', open: false })
  }

  const onDelete = async (id: number) => {
    try {
      await deleteRule({ id: id });
      ref.current?.reload();
      message.success('删除成功');
    } catch (error) {
      message.error('删除失败请重试！');
      return false;
    }
  }

  const buildVersionUrl = (record: RULE.Rule) => {
    return '/rule/' + record.id + '/version?rule=' + record.name;
  }

  const columns: ProColumns<RULE.Rule>[] = [
    {
      title: '序号',
      dataIndex: 'id',
      key: 'id',
      align: 'center',
      search: false
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
      search: false,
      width: '100px',
      render: (_, record) => (
        <Space size='small'>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => history.push(buildVersionUrl(record))}>规则版本</Button>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => onUpdate(record)}>编辑</Button>
          <Button style={{ padding: '0 4px' }} type='link' onClick={() => onDelete(record.id)}>删除</Button>
        </Space>
      ),
    },
  ];


  return (
    <>
      <ProTable<RULE.Rule>
        actionRef={ref}
        search={{ collapseRender: false }}
        options={false}
        toolBarRender={() => [
          <Button key="1" type="primary" onClick={() => onCreate()}>
            新建
          </Button>
        ]}
        columns={columns}
        request={async (params) => {
          const { data, success } = await queryRule({
            ...params, 
            pageSize: params.pageSize, 
            pageNumber: params.current,
          });
          return {
            data: data?.list || [],
            success,
          };
        }} />
      {modal.scene === 'create' && <RuleModal
        open={modal.open}
        title={modal.title}
        onCancel={() => onCancel()}
        onCommit={(val) => onCreateSave(val)}
      />}
      {modal.scene === 'update' && <RuleModal
        open={modal.open}
        title={modal.title}
        onCancel={() => onCancel()}
        onCommit={(val) => onUpdateSave(val)} />}
    </>
  );
};


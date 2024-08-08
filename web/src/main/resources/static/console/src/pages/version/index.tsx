import { Button, message, Space } from 'antd';
import { useRef, useState } from 'react';
import { useMatch, history } from '@umijs/max';
import { ActionType, ProColumns, ProTable } from '@ant-design/pro-components';
import RuleVersionModal from './modal';
import RuleVersionValid from './valid';
import services from '@/services/version';
const { addRuleVersion,
  deleteRuleVersion,
  updateRuleVersion,
  queryRuleVersion,
  deployRuleVersion,
  undeployRuleVersion 
} =
  services.RuleVersionApi;

const RuleVersionPage = () => {
  const [modal, setModal] = useState<{
    scene: string
    open: boolean,
    title?: string,
    initValue?: VERSION.RuleVersion
  }>({ scene: 'create', open: false, title: '创建版本' })

  const [valid, setValid] = useState<{
    open: boolean,
    record?: VERSION.RuleVersion
  }>({ open: false })

  const ref = useRef<ActionType>();

  const match = useMatch('/rule/:ruleId/version')
  let ruleId = match?.params.ruleId;
  if (ruleId === undefined) {
    history.push('/rule/index')
    return;
  }
  const intRuleId = parseInt(ruleId);

  const onCreate = () => {
    setModal({ scene: 'create', open: true, title: '创建版本', });
  }

  const onCreateSave = async (record: VERSION.RuleVersion) => {
    try {
      await addRuleVersion({ ruleId: intRuleId }, record);
      message.success('创建成功');
      setModal({ scene: 'create', open: false, title: '创建版本', });
      ref.current?.reload();
    } catch (error) {
      message.error('创建失败请重试！');
    }
  }

  const onUpdate = (record: VERSION.RuleVersion) => {
    setModal({ scene: 'update', open: true, title: '编辑版本', initValue: record });
  }

  const onUpdateSave = async (record: VERSION.RuleVersion) => {
    try {
      await updateRuleVersion({ ruleId: intRuleId, id: record.id }, record);
      message.success('修改成功');
      setModal({ scene: 'create', open: false, title: '编辑版本', });
      ref.current?.reload();
    } catch (error) {
      message.error('修改失败请重试！');
    }
  }

  const onCancel = () => {
    setModal({ scene: 'create', open: false })
  }

  const onDelete = async (id: number) => {
    try {
      await deleteRuleVersion({ id: id })
      message.success('删除成功');
      ref.current?.reload();
    } catch (error) {
      message.error('删除失败请重试！');
    }
  }

  const onDeploy = async (recordId: number) => {
    try {
      await deployRuleVersion({ id: recordId })
      message.success('部署成功');
      ref.current?.reload();
    } catch (error) {
      message.error('部署失败请重试！');
    }
  }

  const onUnDeploy = async (recordId: number) => {
    try {
      await undeployRuleVersion({ id: recordId })
      message.success('下线成功');
      ref.current?.reload();
    } catch (error) {
      message.error('下线失败请重试！');
    }
  }

  const onValid = (record: VERSION.RuleVersion) => {
    setValid({ open: true, record: record })
  }

  function toDesign(record: VERSION.RuleVersion): void {
    const url = '/rule/' + match?.params.ruleId + '/version/' + record.id + '/design/factClass';
    history.push(url);
  }

  const columns: ProColumns<VERSION.RuleVersion>[] = [
    {
      title: '序号',
      dataIndex: 'id',
      search: false,
      key: 'id',
      render: (text) => <a>{text}</a>,
    },
    {
      title: '版本号',
      dataIndex: 'version',
      search: false,
      key: 'version',
    },
    {
      title: '版本说明',
      dataIndex: 'description',
      search: false,
      key: 'description',
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      search: false,
      key: 'createTime',
    },
    {
      title: '操作',
      search: false,
      key: 'action',
      width: '100px',
      render: (_, record) => (
        <Space size="small">
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => toDesign(record)}>设计</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onUpdate(record)}>编辑</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onValid(record)}>验证</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onDeploy(record.id)}>部署</Button>
          <Button style={{ padding: '0 2px' }} type='link' onClick={() => onUnDeploy(record.id)}>下线</Button>
          <Button style={{ padding: '0 2px' }} type='link' color='danger' onClick={() => onDelete(record.id)}>删除</Button>
        </Space>
      ),
    },
  ];

  return (
    <>
      <ProTable<VERSION.RuleVersion>
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
          const { data, success } = await queryRuleVersion({
            ...params, ruleId: intRuleId
          });
          return {
            data: data || [],
            success,
          };
        }} />
      {
        modal.scene === 'create' && <RuleVersionModal open={modal.open}
          title={modal.title}
          initValue={modal.initValue}
          onCancel={() => onCancel()}
          onCommit={(val) => onCreateSave(val)}
        >
        </RuleVersionModal>
      }
      {
        modal.scene === 'update' && <RuleVersionModal open={modal.open}
          title={modal.title}
          initValue={modal.initValue}
          onCancel={() => onCancel()}
          onCommit={(val) => onUpdateSave(val)}
        >
        </RuleVersionModal>
      }

      <RuleVersionValid
        open={valid.open}
        record={valid.record}
      >
      </RuleVersionValid>
    </>
  );
};

export default RuleVersionPage;
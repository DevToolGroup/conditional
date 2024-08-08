import { Form, Input, Modal } from "antd"

interface RuleModalProps {
  open: boolean
  title?: string
  initValue? : any
  onCancel: () => void
  onCommit: (values: any) => void
}

const RuleModal = (props: RuleModalProps) => {
  const [form] = Form.useForm();
  const {open, title, initValue, onCancel, onCommit} = props;

  if (initValue !== undefined) {
    form.setFieldsValue(initValue);
  } else {
    form.resetFields();
  }

  return (<>
    <Modal
      forceRender
      open={open}
      title={title}
      okText="提交"
      cancelText="取消"
      okButtonProps={{ autoFocus: true, htmlType: 'submit' }}
      onCancel={onCancel}
      destroyOnClose
      modalRender={(dom) => (
        <Form layout="vertical" form={form} name="form_in_modal"
          clearOnDestroy
          onFinish={(values) => onCommit(values)}>
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
      <Form.Item name="name" label="规则名称" rules={[{ required: true, message: '请输入规则名称' }]}>
        <Input placeholder="请输入规则名称" />
      </Form.Item>
      <Form.Item name="description" label="描述" rules={[{ required: true, message: '请输入规则表述' }]}>
        <Input.TextArea rows={4} placeholder="请输入规则表述" />
      </Form.Item>
    </Modal>
  </>)
}

export default RuleModal
import { Form, Input, Modal } from "antd"

interface RuleVersionModalProps {
  open: boolean
  title?: string
  initValue? : any

  onCancel: () => void
  onCommit: (values: any) => void
}


const RuleVersionModal = (props: RuleVersionModalProps) => {
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
      onCancel={() => onCancel()}
      destroyOnClose
      modalRender={(dom) => (
        <Form layout="vertical" form={form} name="form_in_modal"
          initialValues={{ modifier: 'public' }} clearOnDestroy
          onFinish={(values) => onCommit(values)}>
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
  </>)
}

export default RuleVersionModal;
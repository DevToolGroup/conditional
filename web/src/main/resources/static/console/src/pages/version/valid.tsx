import { Button, Col, Drawer, Form, Input, message, Row, Space } from "antd";
import services from '@/services/version';
const {
  validateRuleVersion
} =
  services.RuleVersionApi;

interface RuleVersionValidateProps {
  open?: boolean;
  onClose?: (e: React.MouseEvent | React.KeyboardEvent) => void;
  record?: VERSION.RuleVersion
}

const RuleVersionValidate = ({ open = false, onClose, record }: RuleVersionValidateProps) => {
  const [form] = Form.useForm();

  form.setFieldsValue(record);
  const onTest = async (values: VERSION.RuleVersionValidateResult) => {
    try {
      await validateRuleVersion({ id: values.id, ruleId: values.ruleId })
      message.error("验证成功")
    } catch(error) {
      message.error("验证失败，请重试")
    }
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
            <Form.Item name="id">
              <Input hidden />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={24}>
            <Form.Item name="ruleId">
              <Input hidden />
            </Form.Item>
          </Col>
        </Row>
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

export default RuleVersionValidate;
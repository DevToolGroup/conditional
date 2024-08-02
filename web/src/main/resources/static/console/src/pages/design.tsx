import { Tabs, TabsProps } from "antd";

import { Outlet, useMatch, useSearchParams, history, useLocation } from "umi";

export default function DesignPage() {
  const location = useLocation();

  let versionDescription;
  if (location.state) {
    console.log(`Received data:`, location.state);
    versionDescription = (location.state as { description: string }).description ?? '';
  }
  console.log('Version Description:', versionDescription);

  const [searchParams] = useSearchParams();
  const match = useMatch('/rule/:ruleId/version/:versionId/:class')

  const items: TabsProps['items'] = [
    {
      key: 'factClass',
      label: '事实定义',
    },
    {
      key: 'argumentClass',
      label: '参数定义',
    },
    {
      key: 'returnClass',
      label: '结果定义',
    },
    {
      key: 'variableClass',
      label: '变量定义',
    },
    {
      key: 'conditionClass',
      label: '条件定义',
    },
  ];

  const onChange = (tab: string) => {
    let path = '/rule/' + match?.params.ruleId + '/version/' + match?.params.versionId + '/design/' + tab;
    history.push(path);
  }

  return (
    <>
      <div>
        <div style={{ fontSize: 14, fontWeight: 600, margin: '16px 0' }}>
          规则：{searchParams.get("rule")}
        </div>
        <div style={{ fontSize: 14, fontWeight: 600, margin: '16px 0' }}>
          版本说明：{versionDescription}
        </div>
      </div>
      <Tabs activeKey={match?.params.class} items={items} onChange={onChange}></Tabs>
      <Outlet></Outlet>
    </>
  )
}
// 运行时配置
// 全局初始化数据配置，用于 Layout 用户信息和权限初始化

import { AvatarProps, Dropdown } from "antd";
import { LogoutOutlined} from '@ant-design/icons';
import { ReactNode } from "react";

// 更多信息见文档：https://umijs.org/docs/api/runtime-config#getinitialstate
export async function getInitialState(): Promise<{ name: string }> {
  return { name: '@umijs/max' };
}

export const layout = () => {
  return {
    logo: 'https://img.alicdn.com/tfs/TB1YHEpwUT1gK0jSZFhXXaAtVXa-28-27.svg',
    menu: {
      locale: false,
    },
    layout: 'mix',
    avatarProps: {
      src: 'https://gw.alipayobjects.com/zos/antfincdn/efFD%24IOql2/weixintupian_20170331104822.jpg',
      size: 'small',
      title: '七妮妮',
      render: (props: AvatarProps, dom: ReactNode) => {
        return (
          <Dropdown
            menu={{
              items: [
                {
                  key: 'logout',
                  icon: <LogoutOutlined />,
                  label: '退出登录',
                },
              ],
            }}
          >
            {dom}
          </Dropdown>
        );
      },
    },
  };
};

import { PageContainer, ProBreadcrumb } from "@ant-design/pro-components";
import { Link, Outlet, useLocation } from "@umijs/max";
import { ConfigProvider } from "antd";

const getItems = (location: any) =>{
  let items = [];
  if (location.pathname.includes('/design')) {
    items.push({ path: '/rule', title: '规则' });
    items.push({ path: location.pathname.split('/').slice(2, 4).join('/'), title: '版本' });
    items.push({ path: location.pathname.split('/').slice(4).join('/'), title: '设计' });
  } else if (location.pathname.includes('/version')) {
    items.push({ path: '/rule', title: '规则' });
    items.push({ path: location.pathname, title: '版本' });
  } else {
    items.push({ path: '/rule', title: '规则' });
  }
  return items;
}

const BootstrapPage = () => {
  const location = useLocation();
  const items = getItems(location);

  const itemRender = (currentRoute: any, params: any, items: any, paths: any) => {
    console.log(location.pathname);
    const isLast = currentRoute?.path === items[items.length - 1]?.path;
    return isLast ? (
      <span>{currentRoute.title}</span>
    ) : (
      <Link to={paths.join("/")}>{currentRoute.title}</Link>
    );
  }

  return (<>
    <ConfigProvider theme={{ token: { borderRadius: 0 } }}>
      <PageContainer title={'规则定义'}>
        <Outlet></Outlet>
      </PageContainer>
    </ConfigProvider>

  </>)
}

export default BootstrapPage;
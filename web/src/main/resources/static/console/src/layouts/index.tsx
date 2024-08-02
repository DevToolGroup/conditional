import { PageContainer, ProBreadcrumb, ProCard, ProLayout } from '@ant-design/pro-components';
import debug from '@umijs/utils/compiled/debug';
import { Link, Outlet, useAppData, useLocation, history } from 'umi';

export default function Layout() {
  const location = useLocation();
  const items = getItems(location);
  function getItems(location: any) {
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
    console.log(items);
    return items;
  }
  function itemRender(currentRoute: any, params: any, items: any, paths: any) {
    console.log("paths", paths.join("/"))
    const isLast = currentRoute?.path === items[items.length - 1]?.path;
    return isLast ? (
      <span>{currentRoute.title}</span>
    ) : (
      <Link to={paths.join("/")}>{currentRoute.title}</Link>
    );
  }

  return (
    <ProLayout
      layout='top'
      logo={false}
      title="规则引擎"
      avatarProps={{
        src: 'https://gw.alipayobjects.com/zos/antfincdn/efFD%24IOql2/weixintupian_20170331104822.jpg',
        size: 'small',
        title: 'DevTool Group',
      }}>
      <PageContainer title={false} breadcrumbRender={() => {
        return <ProBreadcrumb itemRender={itemRender} items={items} />;
      }}>
        <ProCard
          style={{
            minHeight: '80vh',
            maxHeight: '100vh',
          }}
        >
          <Outlet />
        </ProCard>
      </PageContainer>
    </ProLayout>
  );
}
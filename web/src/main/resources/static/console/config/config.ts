import { defineConfig } from '@umijs/max';

export default defineConfig({
  antd: {},
  access: {},
  model: {},
  initialState: {},
  request: {},
  layout: {
    title: '@umijs/max',
  },
  routes: [
    {
      path: '/rule/',
      component: 'bootstrap',
      name: '规则定义',
      routes: [
        {
          path: '/rule/index',
          component: './rule',
        },
        {
          path: '/rule/:ruleId/version',
          component: './version',
        },
        {
          path: '/rule/:ruleId/version/:versionId/design',
          component: './design',
          routes: [
            {
              path: 'factClass',
              component: './design/factClass',
            },
            {
              path: 'argumentClass',
              component: './design/argumentClass',
            },
            {
              path: 'returnClass',
              component: './design/returnClass',
            },
            {
              path: 'variableClass',
              component: './design/variableClass',
            },
            {
              path: 'conditionClass',
              component: './design/conditionClass',
            },
          ],
        },
        {
          path: '/rule/*', redirect: '/rule/index'
        }
      ],
    },
    { 
      path: '/', 
      redirect: '/rule/index' 
    },
  ],
  proxy: {
    '/conditional/api/': {
      target: 'http://localhost:8080/condition/api/',
      changeOrigin: true,
      pathRewrite: {'^/conditional/api/': ''}
    }
  },
  npmClient: 'pnpm',
});

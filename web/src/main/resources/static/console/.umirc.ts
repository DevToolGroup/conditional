import { defineConfig } from "umi";

export default defineConfig({
  routes: [
    { 
      path: "/rule", 
      component: "rule",
      name: "规则定义",
    },
    {
      path: "/rule/:ruleId/version",
      component: "version",
      name: "规则版本",
    },
    {
      path: "/rule/:ruleId/version/:versionId/design",
      component: "design/index",
      name: "规则设计",
      routes: [
        {
          path: "factClass",
          component: "design/factClass",
        },
        {
          path: "argumentClass",
          component: "design/argumentClass"
        },
        {
          path: "returnClass",
          component: "design/returnClass"
        },
        {
          path: "variableClass",
          component: "design/variableClass"
        },
        {
          path: "conditionClass",
          component: "design/conditionClass"
        },
      ]
    },
    { path: '/', redirect: '/rule' },
  ],
  plugins: ["@umijs/plugins/dist/react-query"],
  reactQuery: {},
  npmClient: "yarn",
});



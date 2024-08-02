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
      component: "design",
      name: "规则设计",
      routes: [
        {
          path: "factClass",
          component: "factClass",
        },
        {
          path: "argumentClass",
          component: "argumentClass"
        },
        {
          path: "returnClass",
          component: "returnClass"
        },
        {
          path: "variableClass",
          component: "variableClass"
        },
        {
          path: "conditionClass",
          component: "conditionClass"
        },
      ]
    },
    { path: '/', redirect: '/rule' },
  ],
  plugins: ["@umijs/plugins/dist/react-query"],
  reactQuery: {},
  npmClient: "yarn",
});



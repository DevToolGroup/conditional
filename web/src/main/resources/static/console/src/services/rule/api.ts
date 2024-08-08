import { request } from '@umijs/max';

export async function queryRule(
  params: {
    code?: string;
    name?: string;
    pageNumber?: number;
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<RULE.RulePageResult>('/conditional/api/rule', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function addRule(
  body?: RULE.Rule,
  options?: { [key: string]: any },
) {
  return request<RULE.RuleResult>('/conditional/api/rule', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

export async function getRule(
  params: {
    id?: number;
  },
  options?: { [key: string]: any },
) {
  const { id: ruleId } = params;
  return request<RULE.RuleResult>(`/conditional/api/rule/${ruleId}`, {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 PUT /api/v1/user/${param0} */
export async function updateRule(
  params: {
    id?: number;
  },
  body?: RULE.Rule,
  options?: { [key: string]: any },
) {
  const { id: ruleId } = params;
  return request<RULE.RuleResult>(`/conditional/api/rule/${ruleId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...params },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 DELETE /api/v1/user/${param0} */
export async function deleteRule(
  params: {
    id?: number;
  },
  options?: { [key: string]: any },
) {
  const { id: ruleId } = params;
  return request<RULE.RuleResult>(`/conditional/api/rule/${ruleId}`, {
    method: 'DELETE',
    params: { ...params },
    ...(options || {}),
  });
}

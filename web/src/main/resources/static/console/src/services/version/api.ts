import { request } from '@umijs/max';

export async function queryRuleVersion(
  params: {
    ruleId: number
  },
  options?: { [key: string]: any },
) {
  const { ruleId: ruleId } = params;
  return request<VERSION.RuleVersionPageResponse>(`/conditional/api/rule/${ruleId}/version`, {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

export async function addRuleVersion(
  params: {
    ruleId: number
  },
  body?: VERSION.RuleVersion,
  options?: { [key: string]: any },
) {
  const {ruleId: ruleId} = params;
  return request<VERSION.RuleVersionResponse>(`/conditional/api/rule/${ruleId}/version`, {
    method: 'POST',
    params: {
      ...params
    },
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
export async function getRuleVersion(
  params: {
    ruleId: number;
    id?: number;
  },
  options?: { [key: string]: any },
) {
  const { ruleId: ruleId, id: id } = params;
  return request<VERSION.RuleVersionResponse>(`/conditional/api/rule/${ruleId}/version/${id}`, {
    method: 'GET',
    params: { ...params },
    ...(options || {}),
  });
}

export async function updateRuleVersion(
  params: {
    id: number;
    ruleId: number;
  },
  body?: VERSION.RuleVersion,
  options?: { [key: string]: any },
) {
  const { ruleId: ruleId, id: id } = params;
  return request<RULE.RuleResult>(`/conditional/api/rule/${ruleId}/version/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...params },
    data: body,
    ...(options || {}),
  });
}

export async function deleteRuleVersion(
  params: {
    id?: number;
    ruleId?: number;
  },
  options?: { [key: string]: any },
) {
  const { ruleId: ruleId, id: id } = params;
  return request<RULE.RuleResult>(`/conditional/api/rule/${ruleId}/version/${id}`, {
    method: 'DELETE',
    params: { ...params },
    ...(options || {}),
  });
}

export async function deployRuleVersion(
  params: {
    id?: number;
    ruleId?: number;
  },
  options?: { [key: string]: any },
) {
  const { ruleId: ruleId, id: id } = params;
  const body = {
    id: params.id,
    status: 'DEPLOY'
  }
  return request<RULE.RuleResult>(`/conditional/api/rule/${ruleId}/version/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...params },
    body: body,
    ...(options || {}),
  });
}

export async function undeployRuleVersion(
  params: {
    id?: number;
    ruleId?: number;
  },
  options?: { [key: string]: any },
) {
  const { ruleId: ruleId, id: id } = params;
  const body = {
    id: params.id,
    status: 'UNDEPLOY'
  }
  return request<RULE.RuleResult>(`/conditional/api/rule/${ruleId}/version/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...params },
    body: body,
    ...(options || {}),
  });
}

export async function validateRuleVersion(
  params: {
    id?: number;
    ruleId?: number;
  },
  options?: { [key: string]: any },
) {
  const { ruleId: ruleId, id: id } = params;
  const body = {
    id: params.id,
    status: 'VALID'
  }
  return request<RULE.RuleResult>(`/conditional/api/rule/${ruleId}/version/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    params: { ...params },
    body: body,
    ...(options || {}),
  });
}
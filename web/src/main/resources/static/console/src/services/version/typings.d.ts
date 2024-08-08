declare namespace VERSION {
  interface RuleVersion {
    id: number;
    ruleId: number;
    version: string;
    description: string;
    createTime: string;
  }

  interface RuleVersionPageResponse {
    success?: boolean;
    errorMessage?: string;
    data?: RuleVersion[];
  }

  interface RuleVersionResponse {
    success?: boolean;
    errorMessage?: string;
    data?: RuleVersion;
  }

  interface RuleVersionValidateResult {
    id?: number;
    ruleId?: number;
    code: string;
    argument: string;
    result: string;
  }

  interface RuleVersionValidateResponse {
    success?: boolean;
    errorMessage?: string;
    data?: RuleVersionValidateResult;
  }




}

declare namespace RULE {
  interface Rule {
    id: number;
    code: string;
    name: string;
    description: string;
  }

  interface RuleResult {
    success?: boolean;
    errorMessage?: string;
    data?: Rule;
  }

  interface RulePageResult {
    success?: boolean;
    errorMessage?: string;
    data?: RulePage;
  }

  interface RulePage {
    pageNumber?: number;
    pageSize?: number;
    total?: number;
    list?: Array<Rule>;
  }
}

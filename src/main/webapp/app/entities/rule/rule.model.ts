import { RuleType } from '../rule-type';
import { Process } from '../process';
export class Rule {
    constructor(
        public id?: number,
        public ruleName?: string,
        public ruleArgs?: any,
        public ruleType?: RuleType,
        public process?: Process,
    ) {
    }
}

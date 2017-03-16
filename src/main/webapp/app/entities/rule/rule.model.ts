import { RuleType } from '../rule-type';
import { Process } from '../process';
export class Rule {
    public tab;
    
    constructor(
        public id?: number,
        public ruleName?: string,
        public ruleArgs?: any,
        public ruleType?: RuleType,
        public process?: Process,
    ) {}
    
    public static ruleArgsJson(rule: Rule): String {
        rule.ruleArgs = "{";
        let isInit = false;
        for (let key in rule.tab) {
            if(isInit == true) {
                rule.ruleArgs = rule.ruleArgs + ",";
            }
            rule.ruleArgs = rule.ruleArgs + "\"" + key + "\":\"" + rule.tab[key] + "\"";
            isInit = true;
        }
        rule.ruleArgs = rule.ruleArgs + "}";
        return rule.ruleArgs;
    }
    
}


const enum StatusEnum {
    'Unknown','Success','Running','SoftFail','HardFail','ForceSuccess'
};
import { RuleType } from '../rule-type';
import { RuleTag } from '../rule-tag';
import { Process } from '../process';
export class Rule {
    public tab;
    public ruleReportId:any;
    public processId:any;
    public processName:string;
    public tags?: RuleTag[];

    constructor(
        public id?: number,
        public ruleName?: string,
        public ruleArgs?: any,
        public ruleType?: RuleType,
        public process?: Process,
        public displayStatus?: StatusEnum,
        public enable?: boolean,
    ) { this.tags = []; }

    public static ruleArgsJson(rule: Rule): String {
        let str: string = JSON.stringify(rule.ruleArgs);
        return str;
    }

}

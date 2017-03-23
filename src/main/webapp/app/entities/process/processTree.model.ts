import { Rule } from '../rule/rule.model';

export class ProcessTree {
    constructor(
        public id?: number,
        public processName?: string,
        public childs?: Array<ProcessTree>,
        public rules?: Array<Rule>
    ) {
    }
}

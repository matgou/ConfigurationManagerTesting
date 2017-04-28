import { Rule } from '../rule';
export class RuleTag {
    constructor(
        public id?: number,
        public name?: string,
        public rule?: Rule,
    ) {
    }
}

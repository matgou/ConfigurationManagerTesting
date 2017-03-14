
const enum StatusEnum {
    'Unknow',
    'Ok',
    'SoftFail',
    'HardFail'

};
import { Rule } from '../rule';
export class RuleReport {
    constructor(
        public id?: number,
        public reportDate?: any,
        public status?: StatusEnum,
        public log?: any,
        public rule?: Rule,
    ) {
    }
}


const enum TriggerEnum {
    'cronSchedule',
    'repeatForever',
    'planning'

};
import { Rule } from '../rule';
export class Scheduling {
    constructor(
        public id?: number,
        public trigger?: TriggerEnum,
        public rule?: any,
        public schedulingLabel?: string,
        public rules?: Rule,
    ) {
    }
}

export class RuleType {
    constructor(
        public id?: number,
        public ruleTypeName?: string,
        public checkerBeanName?: string,
        public description?: string,
        public requiredArgumentsList?: string,
        public reportingBeanName?: string,
    ) {
    }
}

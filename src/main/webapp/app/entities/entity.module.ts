import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ConfigurationManagerReportingProcessModule } from './process/process.module';
import { ConfigurationManagerReportingRuleTypeModule } from './rule-type/rule-type.module';
import { ConfigurationManagerReportingRuleModule } from './rule/rule.module';
import { ConfigurationManagerReportingRuleReportModule } from './rule-report/rule-report.module';
import { ConfigurationManagerReportingSchedulingModule } from './scheduling/scheduling.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        ConfigurationManagerReportingProcessModule,
        ConfigurationManagerReportingRuleTypeModule,
        ConfigurationManagerReportingRuleModule,
        ConfigurationManagerReportingRuleReportModule,
        ConfigurationManagerReportingSchedulingModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConfigurationManagerReportingEntityModule {}

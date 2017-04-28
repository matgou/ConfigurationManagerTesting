import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { Order66ProcessModule } from './process/process.module';
import { Order66RuleTypeModule } from './rule-type/rule-type.module';
import { Order66RuleModule } from './rule/rule.module';
import { Order66RuleReportModule } from './rule-report/rule-report.module';
import { Order66SchedulingModule } from './scheduling/scheduling.module';
import { Order66ParamModule } from './param/param.module';
import { Order66RuleTagModule } from './rule-tag/rule-tag.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        Order66ProcessModule,
        Order66RuleTypeModule,
        Order66RuleModule,
        Order66RuleReportModule,
        Order66SchedulingModule,
        Order66ParamModule,
        Order66RuleTagModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Order66EntityModule {}

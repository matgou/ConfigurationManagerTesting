import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ConfigurationManagerReportingSharedModule } from '../../shared';

import {
    RuleService,
    RulePopupService,
    RuleComponent,
    RuleDetailComponent,
    RuleDialogComponent,
    RulePopupComponent,
    RuleDeletePopupComponent,
    RuleDeleteDialogComponent,
    ruleRoute,
    rulePopupRoute,
} from './';

let ENTITY_STATES = [
    ...ruleRoute,
    ...rulePopupRoute,
];

@NgModule({
    imports: [
        ConfigurationManagerReportingSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RuleComponent,
        RuleDetailComponent,
        RuleDialogComponent,
        RuleDeleteDialogComponent,
        RulePopupComponent,
        RuleDeletePopupComponent,
    ],
    entryComponents: [
        RuleComponent,
        RuleDialogComponent,
        RulePopupComponent,
        RuleDeleteDialogComponent,
        RuleDeletePopupComponent,
    ],
    providers: [
        RuleService,
        RulePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConfigurationManagerReportingRuleModule {}

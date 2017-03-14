import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ConfigurationManagerReportingSharedModule } from '../../shared';

import {
    RuleReportService,
    RuleReportPopupService,
    RuleReportComponent,
    RuleReportDetailComponent,
    RuleReportDialogComponent,
    RuleReportPopupComponent,
    RuleReportDeletePopupComponent,
    RuleReportDeleteDialogComponent,
    ruleReportRoute,
    ruleReportPopupRoute,
} from './';

let ENTITY_STATES = [
    ...ruleReportRoute,
    ...ruleReportPopupRoute,
];

@NgModule({
    imports: [
        ConfigurationManagerReportingSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RuleReportComponent,
        RuleReportDetailComponent,
        RuleReportDialogComponent,
        RuleReportDeleteDialogComponent,
        RuleReportPopupComponent,
        RuleReportDeletePopupComponent,
    ],
    entryComponents: [
        RuleReportComponent,
        RuleReportDialogComponent,
        RuleReportPopupComponent,
        RuleReportDeleteDialogComponent,
        RuleReportDeletePopupComponent,
    ],
    providers: [
        RuleReportService,
        RuleReportPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConfigurationManagerReportingRuleReportModule {}

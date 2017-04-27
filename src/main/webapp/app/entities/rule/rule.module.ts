import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Order66SharedModule } from '../../shared';

import {
    RuleService,
    RulePopupService,
    RuleComponent,
    RuleDetailComponent,
    RuleDialogComponent,
    RulePopupComponent,
    KeysPipe,
    SplitStringPipe,
    RuleExecuteComponent,
    RuleExecutePopupComponent,
    RuleDeletePopupComponent,
    RuleDeleteDialogComponent,
    RuleForceAckComponent,
    RuleForceAckPopupComponent,
    ruleRoute,
    rulePopupRoute,
} from './';

let ENTITY_STATES = [
    ...ruleRoute,
    ...rulePopupRoute,
];

@NgModule({
    imports: [
        Order66SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RuleComponent,
        RuleDetailComponent,
        RuleDialogComponent,
        RuleDeleteDialogComponent,
        RuleExecuteComponent,
        RuleExecutePopupComponent,
        RulePopupComponent,
        RuleDeletePopupComponent,
        KeysPipe,
        SplitStringPipe,
        RuleForceAckComponent,
        RuleForceAckPopupComponent,
    ],
    entryComponents: [
        RuleComponent,
        RuleDialogComponent,
        RulePopupComponent,
        RuleExecuteComponent,
        RuleExecutePopupComponent,
        RuleForceAckComponent,
        RuleForceAckPopupComponent,
        RuleDeleteDialogComponent,
        RuleDeletePopupComponent,
    ],
    providers: [
        RuleService,
        RulePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Order66RuleModule {}

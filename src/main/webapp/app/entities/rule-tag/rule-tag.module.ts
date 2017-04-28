import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Order66SharedModule } from '../../shared';

import {
    RuleTagService,
    RuleTagPopupService,
    RuleTagComponent,
    RuleTagDetailComponent,
    RuleTagDialogComponent,
    RuleTagPopupComponent,
    RuleTagDeletePopupComponent,
    RuleTagDeleteDialogComponent,
    ruleTagRoute,
    ruleTagPopupRoute,
} from './';

let ENTITY_STATES = [
    ...ruleTagRoute,
    ...ruleTagPopupRoute,
];

@NgModule({
    imports: [
        Order66SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        RuleTagComponent,
        RuleTagDetailComponent,
        RuleTagDialogComponent,
        RuleTagDeleteDialogComponent,
        RuleTagPopupComponent,
        RuleTagDeletePopupComponent,
    ],
    entryComponents: [
        RuleTagComponent,
        RuleTagDialogComponent,
        RuleTagPopupComponent,
        RuleTagDeleteDialogComponent,
        RuleTagDeletePopupComponent,
    ],
    providers: [
        RuleTagService,
        RuleTagPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Order66RuleTagModule {}

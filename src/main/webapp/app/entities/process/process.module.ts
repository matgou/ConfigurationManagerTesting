import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Order66SharedModule } from '../../shared';

import {
    ProcessService,
    ProcessPopupService,
    ProcessComponent,
    ProcessDetailComponent,
    ProcessDialogComponent,
    ProcessPopupComponent,
    ProcessDeletePopupComponent,
    ProcessDeleteDialogComponent,
    processRoute,
    processPopupRoute,
} from './';

let ENTITY_STATES = [
    ...processRoute,
    ...processPopupRoute,
];

@NgModule({
    imports: [
        Order66SharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProcessComponent,
        ProcessDetailComponent,
        ProcessDialogComponent,
        ProcessDeleteDialogComponent,
        ProcessPopupComponent,
        ProcessDeletePopupComponent,
    ],
    entryComponents: [
        ProcessComponent,
        ProcessDialogComponent,
        ProcessPopupComponent,
        ProcessDeleteDialogComponent,
        ProcessDeletePopupComponent,
    ],
    providers: [
        ProcessService,
        ProcessPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Order66ProcessModule {}

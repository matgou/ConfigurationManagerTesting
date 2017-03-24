import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Order66SharedModule } from '../shared';

import { ToBadgePipe, ToClassPipe, ToRuleSummary, SUMMARY_ROUTE, SummaryComponent } from './';


@NgModule({
    imports: [
        Order66SharedModule,
        RouterModule.forRoot([ SUMMARY_ROUTE ], { useHash: true })
    ],
    declarations: [
        SummaryComponent,
        ToClassPipe,
        ToBadgePipe,
        ToRuleSummary
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Order66SummaryModule {}

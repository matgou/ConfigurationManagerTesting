import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ConfigurationManagerReportingSharedModule } from '../shared';

import { SUMMARY_ROUTE, SummaryComponent } from './';


@NgModule({
    imports: [
        ConfigurationManagerReportingSharedModule,
        RouterModule.forRoot([ SUMMARY_ROUTE ], { useHash: true })
    ],
    declarations: [
        SummaryComponent,
    ],
    entryComponents: [
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConfigurationManagerReportingSummaryModule {}

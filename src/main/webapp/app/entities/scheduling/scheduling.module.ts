import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ConfigurationManagerReportingSharedModule } from '../../shared';
import { CronWidgetInputComponent } from '../../cron-widget-input/cron-widget-input.component';

import {
    SchedulingService,
    SchedulingPopupService,
    SchedulingComponent,
    SchedulingDetailComponent,
    SchedulingDialogComponent,
    SchedulingPopupComponent,
    SchedulingDeletePopupComponent,
    SchedulingDeleteDialogComponent,
    schedulingRoute,
    schedulingPopupRoute,
    SchedulingResolvePagingParams,
} from './';

let ENTITY_STATES = [
    ...schedulingRoute,
    ...schedulingPopupRoute,
];

@NgModule({
    imports: [
        ConfigurationManagerReportingSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SchedulingComponent,
        SchedulingDetailComponent,
        SchedulingDialogComponent,
        SchedulingDeleteDialogComponent,
        SchedulingPopupComponent,
        SchedulingDeletePopupComponent,
        CronWidgetInputComponent,
    ],
    entryComponents: [
        SchedulingComponent,
        SchedulingDialogComponent,
        SchedulingPopupComponent,
        SchedulingDeleteDialogComponent,
        SchedulingDeletePopupComponent,
    ],
    providers: [
        SchedulingService,
        SchedulingPopupService,
        SchedulingResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ConfigurationManagerReportingSchedulingModule {}

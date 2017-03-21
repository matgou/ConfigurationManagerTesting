import './vendor.ts';

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { ConfigurationManagerReportingSharedModule, UserRouteAccessService } from './shared';
import { ConfigurationManagerReportingHomeModule } from './home/home.module';
import { ConfigurationManagerReportingAdminModule } from './admin/admin.module';
import { ConfigurationManagerReportingAccountModule } from './account/account.module';
import { ConfigurationManagerReportingEntityModule } from './entities/entity.module';

import { LayoutRoutingModule } from './layouts';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';


@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        ConfigurationManagerReportingSharedModule,
        ConfigurationManagerReportingHomeModule,
        ConfigurationManagerReportingAdminModule,
        ConfigurationManagerReportingAccountModule,
        ConfigurationManagerReportingEntityModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent,
    ],
    providers: [
        ProfileService,
        { provide: Window, useValue: window },
        { provide: Document, useValue: document },
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class ConfigurationManagerReportingAppModule {}

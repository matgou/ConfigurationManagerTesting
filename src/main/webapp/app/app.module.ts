import './vendor.ts';

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { Order66SharedModule, UserRouteAccessService } from './shared';
import { Order66HomeModule } from './home/home.module';
import { Order66AdminModule } from './admin/admin.module';
import { Order66SummaryModule } from './summary/summary.module';
import { Order66AccountModule } from './account/account.module';
import { Order66EntityModule } from './entities/entity.module';
import { WebsocketService } from './websocket.service';

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
        Order66SharedModule,
        Order66HomeModule,
        Order66AdminModule,
        Order66AccountModule,
        Order66EntityModule,
        Order66SummaryModule
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
        UserRouteAccessService,
        WebsocketService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class Order66AppModule {}

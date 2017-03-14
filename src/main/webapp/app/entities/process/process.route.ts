import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ProcessComponent } from './process.component';
import { ProcessDetailComponent } from './process-detail.component';
import { ProcessPopupComponent } from './process-dialog.component';
import { ProcessDeletePopupComponent } from './process-delete-dialog.component';

import { Principal } from '../../shared';


export const processRoute: Routes = [
  {
    path: 'process',
    component: ProcessComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Processes'
    }
  }, {
    path: 'process/:id',
    component: ProcessDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Processes'
    }
  }
];

export const processPopupRoute: Routes = [
  {
    path: 'process-new',
    component: ProcessPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Processes'
    },
    outlet: 'popup'
  },
  {
    path: 'process/:id/edit',
    component: ProcessPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Processes'
    },
    outlet: 'popup'
  },
  {
    path: 'process/:id/delete',
    component: ProcessDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Processes'
    },
    outlet: 'popup'
  }
];

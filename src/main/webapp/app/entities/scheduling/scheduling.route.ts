import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { SchedulingComponent } from './scheduling.component';
import { SchedulingDetailComponent } from './scheduling-detail.component';
import { SchedulingPopupComponent } from './scheduling-dialog.component';
import { SchedulingDeletePopupComponent } from './scheduling-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class SchedulingResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const schedulingRoute: Routes = [
  {
    path: 'scheduling',
    component: SchedulingComponent,
    resolve: {
      'pagingParams': SchedulingResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Schedulings'
    }
  }, {
    path: 'scheduling/:id',
    component: SchedulingDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Schedulings'
    }
  }
];

export const schedulingPopupRoute: Routes = [
  {
    path: 'scheduling-new',
    component: SchedulingPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Schedulings'
    },
    outlet: 'popup'
  },
  {
    path: 'scheduling/:id/edit',
    component: SchedulingPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Schedulings'
    },
    outlet: 'popup'
  },
  {
    path: 'scheduling/:id/delete',
    component: SchedulingDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Schedulings'
    },
    outlet: 'popup'
  }
];

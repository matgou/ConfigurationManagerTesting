import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ParamComponent } from './param.component';
import { ParamDetailComponent } from './param-detail.component';
import { ParamPopupComponent } from './param-dialog.component';
import { ParamDeletePopupComponent } from './param-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ParamResolvePagingParams implements Resolve<any> {

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

export const paramRoute: Routes = [
  {
    path: 'param',
    component: ParamComponent,
    resolve: {
      'pagingParams': ParamResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Params'
    }
  }, {
    path: 'param/:id',
    component: ParamDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Params'
    }
  }
];

export const paramPopupRoute: Routes = [
  {
    path: 'param-new',
    component: ParamPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Params'
    },
    outlet: 'popup'
  },
  {
    path: 'param/:id/edit',
    component: ParamPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Params'
    },
    outlet: 'popup'
  },
  {
    path: 'param/:id/delete',
    component: ParamDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Params'
    },
    outlet: 'popup'
  }
];

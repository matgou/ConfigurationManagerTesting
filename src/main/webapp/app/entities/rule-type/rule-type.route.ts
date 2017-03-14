import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { RuleTypeComponent } from './rule-type.component';
import { RuleTypeDetailComponent } from './rule-type-detail.component';
import { RuleTypePopupComponent } from './rule-type-dialog.component';
import { RuleTypeDeletePopupComponent } from './rule-type-delete-dialog.component';

import { Principal } from '../../shared';


export const ruleTypeRoute: Routes = [
  {
    path: 'rule-type',
    component: RuleTypeComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTypes'
    }
  }, {
    path: 'rule-type/:id',
    component: RuleTypeDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTypes'
    }
  }
];

export const ruleTypePopupRoute: Routes = [
  {
    path: 'rule-type-new',
    component: RuleTypePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTypes'
    },
    outlet: 'popup'
  },
  {
    path: 'rule-type/:id/edit',
    component: RuleTypePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTypes'
    },
    outlet: 'popup'
  },
  {
    path: 'rule-type/:id/delete',
    component: RuleTypeDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTypes'
    },
    outlet: 'popup'
  }
];

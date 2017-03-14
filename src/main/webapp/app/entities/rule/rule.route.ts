import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { RuleComponent } from './rule.component';
import { RuleDetailComponent } from './rule-detail.component';
import { RulePopupComponent } from './rule-dialog.component';
import { RuleDeletePopupComponent } from './rule-delete-dialog.component';

import { Principal } from '../../shared';


export const ruleRoute: Routes = [
  {
    path: 'rule',
    component: RuleComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Rules'
    }
  }, {
    path: 'rule/:id',
    component: RuleDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Rules'
    }
  }
];

export const rulePopupRoute: Routes = [
  {
    path: 'rule-new',
    component: RulePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Rules'
    },
    outlet: 'popup'
  },
  {
    path: 'rule/:id/edit',
    component: RulePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Rules'
    },
    outlet: 'popup'
  },
  {
    path: 'rule/:id/delete',
    component: RuleDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Rules'
    },
    outlet: 'popup'
  }
];

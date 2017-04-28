import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { RuleTagComponent } from './rule-tag.component';
import { RuleTagDetailComponent } from './rule-tag-detail.component';
import { RuleTagPopupComponent } from './rule-tag-dialog.component';
import { RuleTagDeletePopupComponent } from './rule-tag-delete-dialog.component';

import { Principal } from '../../shared';


export const ruleTagRoute: Routes = [
  {
    path: 'rule-tag',
    component: RuleTagComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTags'
    }
  }, {
    path: 'rule-tag/:id',
    component: RuleTagDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTags'
    }
  }
];

export const ruleTagPopupRoute: Routes = [
  {
    path: 'rule-tag-new',
    component: RuleTagPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTags'
    },
    outlet: 'popup'
  },
  {
    path: 'rule-tag/:id/edit',
    component: RuleTagPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTags'
    },
    outlet: 'popup'
  },
  {
    path: 'rule-tag/:id/delete',
    component: RuleTagDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleTags'
    },
    outlet: 'popup'
  }
];

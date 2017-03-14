import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { RuleReportComponent } from './rule-report.component';
import { RuleReportDetailComponent } from './rule-report-detail.component';
import { RuleReportPopupComponent } from './rule-report-dialog.component';
import { RuleReportDeletePopupComponent } from './rule-report-delete-dialog.component';

import { Principal } from '../../shared';


export const ruleReportRoute: Routes = [
  {
    path: 'rule-report',
    component: RuleReportComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleReports'
    }
  }, {
    path: 'rule-report/:id',
    component: RuleReportDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleReports'
    }
  }
];

export const ruleReportPopupRoute: Routes = [
  {
    path: 'rule-report-new',
    component: RuleReportPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleReports'
    },
    outlet: 'popup'
  },
  {
    path: 'rule-report/:id/edit',
    component: RuleReportPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleReports'
    },
    outlet: 'popup'
  },
  {
    path: 'rule-report/:id/delete',
    component: RuleReportDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'RuleReports'
    },
    outlet: 'popup'
  }
];

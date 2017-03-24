import { Route } from '@angular/router';

import { UserRouteAccessService } from '../shared';
import { SummaryComponent } from './';

export const SUMMARY_ROUTE: Route = {
  path: 'summary',
  component: SummaryComponent,
  data: {
    authorities: [],
    pageTitle: 'Synthese des verifications'
  },
  canActivate: [UserRouteAccessService]
};

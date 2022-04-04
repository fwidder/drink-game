import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Spiel from './spiel';
import SpielDetail from './spiel-detail';
import SpielUpdate from './spiel-update';
import SpielDeleteDialog from './spiel-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SpielUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SpielUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SpielDetail} />
      <ErrorBoundaryRoute path={match.url} component={Spiel} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SpielDeleteDialog} />
  </>
);

export default Routes;

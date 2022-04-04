import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Getraenk from './getraenk';
import GetraenkDetail from './getraenk-detail';
import GetraenkUpdate from './getraenk-update';
import GetraenkDeleteDialog from './getraenk-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GetraenkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GetraenkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GetraenkDetail} />
      <ErrorBoundaryRoute path={match.url} component={Getraenk} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GetraenkDeleteDialog} />
  </>
);

export default Routes;

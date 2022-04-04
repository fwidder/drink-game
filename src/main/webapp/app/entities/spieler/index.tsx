import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Spieler from './spieler';
import SpielerDetail from './spieler-detail';
import SpielerUpdate from './spieler-update';
import SpielerDeleteDialog from './spieler-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SpielerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SpielerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SpielerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Spieler} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SpielerDeleteDialog} />
  </>
);

export default Routes;

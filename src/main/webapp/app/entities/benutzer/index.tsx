import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Benutzer from './benutzer';
import BenutzerDetail from './benutzer-detail';
import BenutzerUpdate from './benutzer-update';
import BenutzerDeleteDialog from './benutzer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={BenutzerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={BenutzerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={BenutzerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Benutzer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={BenutzerDeleteDialog} />
  </>
);

export default Routes;

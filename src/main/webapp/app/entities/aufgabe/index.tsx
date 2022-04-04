import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Aufgabe from './aufgabe';
import AufgabeDetail from './aufgabe-detail';
import AufgabeUpdate from './aufgabe-update';
import AufgabeDeleteDialog from './aufgabe-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AufgabeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AufgabeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AufgabeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Aufgabe} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AufgabeDeleteDialog} />
  </>
);

export default Routes;

import React from 'react';
import { Switch } from 'react-router-dom';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Spieler from './spieler';
import Spiel from './spiel';
import Benutzer from './benutzer';
import Aufgabe from './aufgabe';
import Getraenk from './getraenk';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default ({ match }) => {
  return (
    <div>
      <Switch>
        {/* prettier-ignore */}
        <ErrorBoundaryRoute path={`${match.url}spieler`} component={Spieler} />
        <ErrorBoundaryRoute path={`${match.url}spiel`} component={Spiel} />
        <ErrorBoundaryRoute path={`${match.url}benutzer`} component={Benutzer} />
        <ErrorBoundaryRoute path={`${match.url}aufgabe`} component={Aufgabe} />
        <ErrorBoundaryRoute path={`${match.url}getraenk`} component={Getraenk} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </Switch>
    </div>
  );
};

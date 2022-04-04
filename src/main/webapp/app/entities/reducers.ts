import spieler from 'app/entities/spieler/spieler.reducer';
import spiel from 'app/entities/spiel/spiel.reducer';
import benutzer from 'app/entities/benutzer/benutzer.reducer';
import aufgabe from 'app/entities/aufgabe/aufgabe.reducer';
import getraenk from 'app/entities/getraenk/getraenk.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  spieler,
  spiel,
  benutzer,
  aufgabe,
  getraenk,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;

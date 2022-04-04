import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/spieler">
        Spieler
      </MenuItem>
      <MenuItem icon="asterisk" to="/spiel">
        Spiel
      </MenuItem>
      <MenuItem icon="asterisk" to="/benutzer">
        Benutzer
      </MenuItem>
      <MenuItem icon="asterisk" to="/aufgabe">
        Aufgabe
      </MenuItem>
      <MenuItem icon="asterisk" to="/getraenk">
        Getraenk
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu as React.ComponentType<any>;

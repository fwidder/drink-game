import { ISpieler } from 'app/shared/model/spieler.model';
import { Kategorie } from 'app/shared/model/enumerations/kategorie.model';
import { Level } from 'app/shared/model/enumerations/level.model';

export interface IAufgabe {
  id?: number;
  name?: string;
  kurztext?: string;
  beschreibung?: string;
  kategorie?: Kategorie;
  level?: Level;
  spielers?: ISpieler[] | null;
}

export const defaultValue: Readonly<IAufgabe> = {};

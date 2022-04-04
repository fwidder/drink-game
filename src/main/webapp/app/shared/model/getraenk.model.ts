import { ISpieler } from 'app/shared/model/spieler.model';
import { Groesse } from 'app/shared/model/enumerations/groesse.model';

export interface IGetraenk {
  id?: number;
  name?: string;
  groesse?: Groesse;
  spielers?: ISpieler[] | null;
}

export const defaultValue: Readonly<IGetraenk> = {};

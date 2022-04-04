import { IBenutzer } from 'app/shared/model/benutzer.model';
import { ISpieler } from 'app/shared/model/spieler.model';

export interface ISpiel {
  id?: number;
  name?: string;
  benutzer?: IBenutzer;
  spielers?: ISpieler[] | null;
}

export const defaultValue: Readonly<ISpiel> = {};

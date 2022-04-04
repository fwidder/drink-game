import { ISpiel } from 'app/shared/model/spiel.model';
import { IGetraenk } from 'app/shared/model/getraenk.model';
import { IAufgabe } from 'app/shared/model/aufgabe.model';

export interface ISpieler {
  id?: number;
  name?: string;
  spiel?: ISpiel;
  getraenks?: IGetraenk[] | null;
  aufgabes?: IAufgabe[] | null;
}

export const defaultValue: Readonly<ISpieler> = {};

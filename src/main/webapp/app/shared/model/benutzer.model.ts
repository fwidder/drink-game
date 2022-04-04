import { IUser } from 'app/shared/model/user.model';
import { ISpiel } from 'app/shared/model/spiel.model';

export interface IBenutzer {
  id?: number;
  name?: string;
  user?: IUser;
  spiels?: ISpiel[] | null;
}

export const defaultValue: Readonly<IBenutzer> = {};

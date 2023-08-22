import { Moment } from 'moment';

export interface ICurrencyResponse {
  id?: number;
  name?: string;
  currency?: string;
  date?: Moment;
}

export class CurrencyResponse implements ICurrencyResponse {
  constructor(
    public id?: number,
    public name?: string,
    public currency?: string,
    public date?: Moment
  ) {
  }
}

import {Moment} from 'moment';

export interface ICurrencyResponse {
  id?: number;
  name?: string;
  currency?: string;
  value?: string;
  date?: string;
}

export class CurrencyResponse implements ICurrencyResponse {
  constructor(
    public id?: number,
    public name?: string,
    public currency?: string,
    public value?: string,
    public date?: string
  ) {
  }
}

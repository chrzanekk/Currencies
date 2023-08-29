export interface ICurrencyRequest {
  name?: string;
  currency?: string;
}

export class CurrencyRequest implements ICurrencyRequest {
  constructor(
    public name?: string,
    public currency?: string
  ) {
  }
}

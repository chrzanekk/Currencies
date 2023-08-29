export interface ICurrencyValueResponse {
  value?: string;
}

export class CurrencyValueResponse implements ICurrencyValueResponse {
  constructor(
    public value?: string,
  ) {}
}

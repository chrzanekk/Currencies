export interface ICurrencyRequest {
  name?: String;
  currency?:String;
}

export class CurrencyRequest implements ICurrencyRequest{
  constructor(
    public name?: String,
    public currency?:String
  ) {}
}

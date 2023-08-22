import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {ICurrencyRequest} from "../models/currency-request.model";
import {map, Observable} from "rxjs";
import {ICurrencyValueResponse} from "../models/currency-value-response.model";
import {FiltrationField} from "../utils/filtration-field-model";
import {FilterType} from "../enumeration/filter-type.model";
import {createRequestOption} from "../utils/request-util";
import {ICurrencyResponse} from "../models/currency-response.model";
import * as moment from 'moment';

type CurrencyEntityResponseType = HttpResponse<ICurrencyRequest>;
type CurrencyValueEntityResponseType = HttpResponse<ICurrencyValueResponse>;
type EntityArrayResponseType = HttpResponse<ICurrencyResponse[]>;


const BASE_API_PATH = 'http://localhost:8080/currencies';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
}
@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  constructor(private http: HttpClient) { }

  createRequest(request :ICurrencyRequest): Observable<CurrencyValueEntityResponseType> {
    return this.http.post<ICurrencyValueResponse>(BASE_API_PATH + '/get-current-currency-value-command', request, {observe:'response'});
  }

  getAllSavedRequest(request?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(request);
    return this.http.get<ICurrencyResponse[]>(BASE_API_PATH + '/requests', {params: options, observe: 'response'})
      .pipe(map((request: EntityArrayResponseType) => this.convertDateArrayFromServer(request)));
  }

  getSearchFields(): FiltrationField[] {
    return [
      {
        id: 'name',
        prefix: 'name',
        type: FilterType.TEXT,
        translateDirective: 'currencies.filterField.name',
        numberInQueue: 0,
      },
      {
        id: 'currency',
        prefix: 'currency',
        type: FilterType.TEXT,
        translateDirective: 'currencies.filterField.currency',
        numberInQueue: 1,
      },
      {
        id: 'valueStartsWith',
        prefix: 'valueStartsWith',
        type: FilterType.TEXT,
        translateDirective: 'currencies.filterField.valueStartsWith',
        numberInQueue: 2,
      },
      {
        id: 'valueEndsWith',
        prefix: 'valueEndsWith',
        type: FilterType.TEXT,
        translateDirective: 'currencies.filterField.valueEndsWith',
        numberInQueue: 3,
      },
      {
        id: 'dateStartsWith',
        prefix: 'dateStartsWith',
        type: FilterType.DATE_TIME_WITH_DATE_PICKER,
        translateDirective: 'currencies.filterField.dateStartsWith',
        numberInQueue: 4,
      },
      {
        id: 'dateEndsWith',
        prefix: 'dateEndsWith',
        type: FilterType.DATE_TIME_WITH_DATE_PICKER,
        translateDirective: 'currencies.filterField.dateEndsWith',
        numberInQueue: 4,
      },
    ];
  }
  protected convertDateArrayFromServer(response: EntityArrayResponseType): EntityArrayResponseType {
    if(response.body) {
      response.body.forEach((currencyResponse: ICurrencyResponse) => {
        currencyResponse.date = currencyResponse.date ? moment(currencyResponse.date) : undefined;
      });
    }
    return response;
  }
}

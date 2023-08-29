import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {ICurrencyRequest} from "../models/currency-request.model";
import {map, Observable} from "rxjs";
import {CurrencyValueResponse, ICurrencyValueResponse} from "../models/currency-value-response.model";
import {CurrencyResponse, ICurrencyResponse} from "../models/currency-response.model";

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

  createRequest(request :ICurrencyRequest): Observable<CurrencyValueResponse> {
    return this.http.post(`${BASE_API_PATH}/get-current-currency-value-command`, request, httpOptions);
  }

  getAllSavedRequest(): Observable<EntityArrayResponseType> {
    return this.http.get<ICurrencyResponse[]>(`${BASE_API_PATH}/requests`, {observe: 'response'})
      .pipe(map((response: EntityArrayResponseType) => this.convertDateArrayFromServer(response)));
  }


  protected convertDateArrayFromServer(response: EntityArrayResponseType): EntityArrayResponseType {
    if(response.body) {
      response.body.forEach((currencyResponse: ICurrencyResponse) => {
        currencyResponse.date = currencyResponse.date ? new Date(currencyResponse.date).toISOString() : undefined;
      });
    }
    return response;
  }
}

import {Component, OnInit} from '@angular/core';
import {CurrencyService} from "../../services/currency.service";
import {Observable, Subscription} from "rxjs";
import {ICurrencyResponse} from "../../models/currency-response.model";
import {HttpResponse} from "@angular/common/http";

type EntityArrayResponseType = HttpResponse<ICurrencyResponse[]>;

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.css']
})
export class CurrencyListComponent implements OnInit {

  result? : Observable<EntityArrayResponseType>;
  currencies : ICurrencyResponse[] | null = [];


  constructor(private currencyService: CurrencyService) {
  }

  ngOnInit() {
    this.getResultFromApi()
  }

  getResultFromApi() {
    this.currencyService.getAllSavedRequest().subscribe(data => {
      this.currencies = data.body;
    });
  }

}

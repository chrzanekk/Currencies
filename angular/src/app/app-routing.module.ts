import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {CurrencyListComponent} from "./components/currency-list/currency-list.component";
import {CurrencyRequest} from "./models/currency-request.model";
import {CurrencyRequestComponent} from "./components/currency-request/currency-request.component";

const routes: Routes = [
  {path: '', redirectTo: 'currencies', pathMatch: 'full'},
  {path: 'currencies', component: CurrencyListComponent},
  {path: 'create-request', component: CurrencyRequestComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

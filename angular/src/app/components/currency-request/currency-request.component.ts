import {Component, OnInit} from '@angular/core';
import {CurrencyRequest, ICurrencyRequest} from "../../models/currency-request.model";
import {CurrencyService} from "../../services/currency.service";
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";


@Component({
  selector: 'app-currency-request',
  templateUrl: './currency-request.component.html',
  styleUrls: ['./currency-request.component.css']
})
export class CurrencyRequestComponent implements OnInit {

  requestForm: FormGroup = new FormGroup({
    name: new FormControl(''),
    currency: new FormControl('')
  });
  submitted = false;

  constructor(private currencyService: CurrencyService, private builder: FormBuilder) {
  }

  ngOnInit() {
    this.requestForm = this.builder.group({
      name: this.builder.control('',
        [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(20)
        ]),
      currency: this.builder.control('',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(3)
        ])
    });
  }

  createRequest(): void {
    this.submitted = true;
    if (this.requestForm.invalid) {
      return;
    } else {
      const request = this.createRequestFromForm();

      this.currencyService.createRequest(request).subscribe({
        next: (response) => {
          alert("Żądanie wysłane poprawnie");
          this.submitted = true;
        },
        error: (e) => alert("ERROR")
      });
    }
  }

  newRequest() {
    this.submitted = false;
    this.requestForm.reset();
  }

  createRequestFromForm(): ICurrencyRequest {
    return {
      ...new CurrencyRequest(),
      name: this.requestForm.get(['name'])!.value,
      currency: this.requestForm.get(['currency'])!.value,
    }
  }

  previousState(): void {
    window.history.back();
  }

  get f(): { [key: string]: AbstractControl } {
    return this.requestForm.controls;
  }

}

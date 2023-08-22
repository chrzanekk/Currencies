import { HttpParams } from '@angular/common/http';

export interface Pagination {
  page: number;
  size: number;
  sort: string[];
}

export interface Search {
  query: string;
}

export interface SearchWithPagination extends Search, Pagination {}

export const createRequestOption = (req?: any): HttpParams => {
  let options: HttpParams = new HttpParams();
  if (req) {
    Object.keys(req).forEach(key => {
      if (key !== 'sort') {
        const value = req[key];
        if ((value || value === 0) && value !== 'undefined' && value !== 'null') {
          options = options.set(key, req[key]);
        }
      }
    });
    if (req.sort) {
      req.sort.forEach((value: string) => {
        if (value && value !== 'undefined' && value !== 'null') {
          options = options.append('sort', value);
        }
      });
    }
  }
  return options;
};

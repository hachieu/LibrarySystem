import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBorrowBook } from 'app/shared/model/borrow-book.model';

type EntityResponseType = HttpResponse<IBorrowBook>;
type EntityArrayResponseType = HttpResponse<IBorrowBook[]>;

@Injectable({ providedIn: 'root' })
export class BorrowBookService {
  public resourceUrl = SERVER_API_URL + 'api/borrow-books';

  constructor(protected http: HttpClient) {}

  create(formData: FormData): Observable<EntityResponseType> {
    return this.http
      .post<IBorrowBook>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(formData: FormData): Observable<EntityResponseType> {
    return this.http
      .put<IBorrowBook>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBorrowBook>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBorrowBook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(borrowBook: IBorrowBook): IBorrowBook {
    const copy: IBorrowBook = Object.assign({}, borrowBook, {
      dateCreate: borrowBook.dateCreate && borrowBook.dateCreate.isValid() ? borrowBook.dateCreate.format(DATE_FORMAT) : undefined,
      dateUpdate: borrowBook.dateUpdate && borrowBook.dateUpdate.isValid() ? borrowBook.dateUpdate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreate = res.body.dateCreate ? moment(res.body.dateCreate) : undefined;
      res.body.dateUpdate = res.body.dateUpdate ? moment(res.body.dateUpdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((borrowBook: IBorrowBook) => {
        borrowBook.dateCreate = borrowBook.dateCreate ? moment(borrowBook.dateCreate) : undefined;
        borrowBook.dateUpdate = borrowBook.dateUpdate ? moment(borrowBook.dateUpdate) : undefined;
      });
    }
    return res;
  }
}

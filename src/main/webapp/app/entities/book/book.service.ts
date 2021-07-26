import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBook } from 'app/shared/model/book.model';

type EntityResponseType = HttpResponse<IBook>;
type EntityArrayResponseType = HttpResponse<IBook[]>;

@Injectable({ providedIn: 'root' })
export class BookService {
  public resourceUrl = SERVER_API_URL + 'api/books';

  constructor(protected http: HttpClient) {}

  create(book: IBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(book);
    return this.http
      .post<IBook>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(book: IBook): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(book);
    return this.http
      .put<IBook>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBook>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBook[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(book: IBook): IBook {
    const copy: IBook = Object.assign({}, book, {
      dateCreate: book.dateCreate && book.dateCreate.isValid() ? book.dateCreate.format(DATE_FORMAT) : undefined,
      dateUpdate: book.dateUpdate && book.dateUpdate.isValid() ? book.dateUpdate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((book: IBook) => {
        book.dateCreate = book.dateCreate ? moment(book.dateCreate) : undefined;
        book.dateUpdate = book.dateUpdate ? moment(book.dateUpdate) : undefined;
      });
    }
    return res;
  }

  findByBookBarCode(bookBarCode: string, status: number): Observable<EntityResponseType> {
    return this.http
      .get<IBook>(`${this.resourceUrl + '/find-barcode'}/${bookBarCode}/${status}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findByBorrowBook(id?: any): Observable<EntityArrayResponseType> {
    return this.http
      .get<IBook[]>(`${this.resourceUrl+"/find-borrow"}/${id}`,{observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  public export(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrl + "/export", { params: options, observe: 'response', responseType: 'text' });
  }

  import(formData: FormData): Observable<EntityResponseType> {
    return this.http
      .post<IBook>(this.resourceUrl+"/import", formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }
}

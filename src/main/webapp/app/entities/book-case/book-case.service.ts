import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBookCase } from 'app/shared/model/book-case.model';

type EntityResponseType = HttpResponse<IBookCase>;
type EntityArrayResponseType = HttpResponse<IBookCase[]>;

@Injectable({ providedIn: 'root' })
export class BookCaseService {
  public resourceUrl = SERVER_API_URL + 'api/book-cases';

  constructor(protected http: HttpClient) {}

  create(bookCase: IBookCase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookCase);
    return this.http
      .post<IBookCase>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bookCase: IBookCase): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookCase);
    return this.http
      .put<IBookCase>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBookCase>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBookCase[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(bookCase: IBookCase): IBookCase {
    const copy: IBookCase = Object.assign({}, bookCase, {
      dateCreate: bookCase.dateCreate && bookCase.dateCreate.isValid() ? bookCase.dateCreate.format(DATE_FORMAT) : undefined,
      dateUpdate: bookCase.dateUpdate && bookCase.dateUpdate.isValid() ? bookCase.dateUpdate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((bookCase: IBookCase) => {
        bookCase.dateCreate = bookCase.dateCreate ? moment(bookCase.dateCreate) : undefined;
        bookCase.dateUpdate = bookCase.dateUpdate ? moment(bookCase.dateUpdate) : undefined;
      });
    }
    return res;
  }
}

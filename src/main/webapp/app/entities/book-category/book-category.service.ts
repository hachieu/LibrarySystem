import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBookCategory } from 'app/shared/model/book-category.model';

type EntityResponseType = HttpResponse<IBookCategory>;
type EntityArrayResponseType = HttpResponse<IBookCategory[]>;

@Injectable({ providedIn: 'root' })
export class BookCategoryService {
  public resourceUrl = SERVER_API_URL + 'api/book-categories';

  constructor(protected http: HttpClient) {}

  create(bookCategory: IBookCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookCategory);
    return this.http
      .post<IBookCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bookCategory: IBookCategory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookCategory);
    return this.http
      .put<IBookCategory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBookCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBookCategory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(bookCategory: IBookCategory): IBookCategory {
    const copy: IBookCategory = Object.assign({}, bookCategory, {
      dateCreate: bookCategory.dateCreate && bookCategory.dateCreate.isValid() ? bookCategory.dateCreate.format(DATE_FORMAT) : undefined,
      dateUpdate: bookCategory.dateUpdate && bookCategory.dateUpdate.isValid() ? bookCategory.dateUpdate.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((bookCategory: IBookCategory) => {
        bookCategory.dateCreate = bookCategory.dateCreate ? moment(bookCategory.dateCreate) : undefined;
        bookCategory.dateUpdate = bookCategory.dateUpdate ? moment(bookCategory.dateUpdate) : undefined;
      });
    }
    return res;
  }
}

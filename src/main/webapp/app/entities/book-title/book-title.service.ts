import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBookTitle } from 'app/shared/model/book-title.model';

type EntityResponseType = HttpResponse<IBookTitle>;
type EntityArrayResponseType = HttpResponse<IBookTitle[]>;

@Injectable({ providedIn: 'root' })
export class BookTitleService {
  public resourceUrl = SERVER_API_URL + 'api/book-titles';

  constructor(protected http: HttpClient) {}

  create(formData: FormData): Observable<EntityResponseType> {
    return this.http
      .post<IBookTitle>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(formData: FormData): Observable<EntityResponseType> {
    return this.http
      .put<IBookTitle>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBookTitle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBookTitle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(bookTitle: IBookTitle): IBookTitle {
    const copy: IBookTitle = Object.assign({}, bookTitle, {
      publicationDate:
        bookTitle.publicationDate && bookTitle.publicationDate.isValid() ? bookTitle.publicationDate.format(DATE_FORMAT) : undefined,
      dateCreate: bookTitle.dateCreate && bookTitle.dateCreate.isValid() ? bookTitle.dateCreate.format(DATE_FORMAT) : undefined,
      dateUpdate: bookTitle.dateUpdate && bookTitle.dateUpdate.isValid() ? bookTitle.dateUpdate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.publicationDate = res.body.publicationDate ? moment(res.body.publicationDate) : undefined;
      res.body.dateCreate = res.body.dateCreate ? moment(res.body.dateCreate) : undefined;
      res.body.dateUpdate = res.body.dateUpdate ? moment(res.body.dateUpdate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bookTitle: IBookTitle) => {
        bookTitle.publicationDate = bookTitle.publicationDate ? moment(bookTitle.publicationDate) : undefined;
        bookTitle.dateCreate = bookTitle.dateCreate ? moment(bookTitle.dateCreate) : undefined;
        bookTitle.dateUpdate = bookTitle.dateUpdate ? moment(bookTitle.dateUpdate) : undefined;
      });
    }
    return res;
  }
}

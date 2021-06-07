import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICard } from 'app/shared/model/card.model';

type EntityResponseType = HttpResponse<ICard>;
type EntityArrayResponseType = HttpResponse<ICard[]>;

@Injectable({ providedIn: 'root' })
export class CardService {
  public resourceUrl = SERVER_API_URL + 'api/cards';

  constructor(protected http: HttpClient) {}

  create(card: ICard): Observable<EntityResponseType> {
    return this.http
      .post<ICard>(this.resourceUrl, card, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(card: ICard): Observable<EntityResponseType> {
    return this.http
      .put<ICard>(this.resourceUrl, card, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICard>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICard[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(card: ICard): ICard {
    const copy: ICard = Object.assign({}, card, {
      dateCreate: card.dateCreate && card.dateCreate.isValid() ? card.dateCreate.format(DATE_FORMAT) : undefined,
      dateUpdate: card.dateUpdate && card.dateUpdate.isValid() ? card.dateUpdate.format(DATE_FORMAT) : undefined,
      dateOfBirth: card.dateOfBirth && card.dateOfBirth.isValid() ? card.dateOfBirth.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreate = res.body.dateCreate ? moment(res.body.dateCreate) : undefined;
      res.body.dateUpdate = res.body.dateUpdate ? moment(res.body.dateUpdate) : undefined;
      res.body.dateOfBirth = res.body.dateOfBirth ? moment(res.body.dateOfBirth) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((card: ICard) => {
        card.dateCreate = card.dateCreate ? moment(card.dateCreate) : undefined;
        card.dateUpdate = card.dateUpdate ? moment(card.dateUpdate) : undefined;
        card.dateOfBirth = card.dateOfBirth ? moment(card.dateOfBirth) : undefined;
      });
    }
    return res;
  }
}

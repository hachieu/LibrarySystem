import { Moment } from 'moment';
import { IBorrowBook } from 'app/shared/model/borrow-book.model';

export interface ICard {
  id?: number;
  codeCard?: string;
  fullName?: string;
  gender?: string;
  identity?: string;
  phoneNumber?: string;
  address?: string;
  dateCreate?: Moment;
  dateUpdate?: Moment;
  userCreate?: string;
  dateOfBirth?: Moment;
  cards?: IBorrowBook[];
}

export class Card implements ICard {
  constructor(
    public id?: number,
    public codeCard?: string,
    public fullName?: string,
    public gender?: string,
    public identity?: string,
    public phoneNumber?: string,
    public address?: string,
    public dateCreate?: Moment,
    public dateUpdate?: Moment,
    public userCreate?: string,
    public dateOfBirth?: Moment,
    public cards?: IBorrowBook[]
  ) {}
}

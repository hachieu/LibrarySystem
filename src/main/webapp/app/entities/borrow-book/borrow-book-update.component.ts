import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBorrowBook, BorrowBook } from 'app/shared/model/borrow-book.model';
import { BorrowBookService } from './borrow-book.service';
import { ICard } from 'app/shared/model/card.model';
import { CardService } from 'app/entities/card/card.service';
import { IBook } from 'app/shared/model/book.model';
import { BookService } from '../book/book.service';

@Component({
  selector: 'jhi-borrow-book-update',
  templateUrl: './borrow-book-update.component.html',
})
export class BorrowBookUpdateComponent implements OnInit {
  isSaving = false;
  cards: ICard[] = [];
  books: IBook[] = [];
  bookBorrowed: IBook[] = [];
  book: IBook | null = null;
  bookBarCode = '';
  status:any;
  chooseStatus?:any;

  editForm = this.fb.group({
    id: [],
    dateCreate: [],
    dateUpdate: [],
    userCreate: [],
    status: [],
    card: [],
  });

  constructor(
    protected borrowBookService: BorrowBookService,
    protected cardService: CardService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private bookService: BookService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ borrowBook }) => {
      this.updateForm(borrowBook);

      if(!this.editForm.get('id')!.value) {
        this.status = 1;
        this.chooseStatus = 0;
      } else {
        this.status = 0;
        this.chooseStatus = 1;
      }

      this.cardService.query().subscribe((res: HttpResponse<ICard[]>) => (this.cards = res.body || []));

      this.bookService.findByBorrowBook(borrowBook.id || 0).subscribe(
        (resBook) => {
          this.bookBorrowed = resBook.body || [];
        }
      );
    });
  }

  chooseBook(): void {
    this.bookService.findByBookBarCode(this.bookBarCode, this.chooseStatus).subscribe(res => {
      this.book = res.body;

      if (this.book) {
        this.books.push(this.book);
      }
    });
  }

  delete(id: any): void {
    this.books.forEach((value, index) => {
      if (value.id === id) {
        this.books.splice(index, 1);
      }
    });
  }

  updateForm(borrowBook: IBorrowBook): void {
    this.editForm.patchValue({
      id: borrowBook.id,
      card: borrowBook.card,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const borrowBook = this.createFromForm();

    const formData: FormData = new FormData();

    formData.append('borrowBook', JSON.stringify(borrowBook));
    formData.append('books', JSON.stringify(this.books));

    if (borrowBook.id !== undefined) {
      this.subscribeToSaveResponse(this.borrowBookService.update(formData));
    } else {
      this.subscribeToSaveResponse(this.borrowBookService.create(formData));
    }
  }

  private createFromForm(): IBorrowBook {
    return {
      ...new BorrowBook(),
      id: this.editForm.get(['id'])!.value,
      card: this.editForm.get(['card'])!.value,
      status: this.status,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBorrowBook>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICard): any {
    return item.id;
  }
}

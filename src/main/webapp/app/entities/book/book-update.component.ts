import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBook, Book } from 'app/shared/model/book.model';
import { BookService } from './book.service';
import { IBookTitle } from 'app/shared/model/book-title.model';
import { BookTitleService } from 'app/entities/book-title/book-title.service';
import { IBookCase } from 'app/shared/model/book-case.model';
import { BookCaseService } from 'app/entities/book-case/book-case.service';
import { IBorrowBook } from 'app/shared/model/borrow-book.model';
import { BorrowBookService } from 'app/entities/borrow-book/borrow-book.service';

type SelectableEntity = IBookTitle | IBookCase | IBorrowBook;

@Component({
  selector: 'jhi-book-update',
  templateUrl: './book-update.component.html',
})
export class BookUpdateComponent implements OnInit {
  isSaving = false;
  booktitles: IBookTitle[] = [];
  bookcases: IBookCase[] = [];
  borrowbooks: IBorrowBook[] = [];
  dateCreateDp: any;
  dateUpdateDp: any;
  status?: number;

  editForm = this.fb.group({
    id: [],
    bookBarCode: [null, [Validators.required]],
    status: [],
    bookTitle: [null, [Validators.required]],
    bookCase: [null, [Validators.required]],
  });

  constructor(
    protected bookService: BookService,
    protected bookTitleService: BookTitleService,
    protected bookCaseService: BookCaseService,
    protected borrowBookService: BorrowBookService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ book }) => {
      this.updateForm(book);

      this.bookTitleService.findAll().subscribe((res: HttpResponse<IBookTitle[]>) => (this.booktitles = res.body || []));

      this.bookCaseService.query().subscribe((res: HttpResponse<IBookCase[]>) => (this.bookcases = res.body || []));

      this.status = book.status || 0;
    });
  }

  updateForm(book: IBook): void {
    this.editForm.patchValue({
      id: book.id,
      bookBarCode: book.bookBarCode,
      status: book.status,
      bookTitle: book.bookTitle,
      bookCase: book.bookCase,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const book = this.createFromForm();
    if (book.id !== undefined) {
      this.subscribeToSaveResponse(this.bookService.update(book));
    } else {
      this.subscribeToSaveResponse(this.bookService.create(book));
    }
  }

  private createFromForm(): IBook {
    return {
      ...new Book(),
      id: this.editForm.get(['id'])!.value,
      bookBarCode: this.editForm.get(['bookBarCode'])!.value,
      status: this.status,
      bookTitle: this.editForm.get(['bookTitle'])!.value,
      bookCase: this.editForm.get(['bookCase'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBook>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}

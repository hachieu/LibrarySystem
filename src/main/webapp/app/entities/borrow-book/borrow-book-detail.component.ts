import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IBook } from 'app/shared/model/book.model';

import { IBorrowBook } from 'app/shared/model/borrow-book.model';
import { BookService } from '../book/book.service';
import { BorrowBookService } from './borrow-book.service';

@Component({
  selector: 'jhi-borrow-book-detail',
  templateUrl: './borrow-book-detail.component.html',
})
export class BorrowBookDetailComponent implements OnInit {
  borrowBook: IBorrowBook | null = null;
  books: IBook[] = [];

  constructor(
    protected activatedRoute: ActivatedRoute, 
    protected borrowBookService: BorrowBookService,
    private bookService: BookService) {}

  ngOnInit(): void {
    this.borrowBookService.find(this.activatedRoute.snapshot.params.id).subscribe(
      (res) => {
        this.borrowBook = res.body;

        this.bookService.findByBorrowBook(this.borrowBook?.id).subscribe(
          (resBook) => {
            this.books = resBook.body || [];
          }
        );
      }
    );
  }

  previousState(): void {
    window.history.back();
  }
}

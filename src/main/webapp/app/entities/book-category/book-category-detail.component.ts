import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBookCategory } from 'app/shared/model/book-category.model';

@Component({
  selector: 'jhi-book-category-detail',
  templateUrl: './book-category-detail.component.html',
})
export class BookCategoryDetailComponent implements OnInit {
  bookCategory: IBookCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookCategory }) => (this.bookCategory = bookCategory));
  }

  previousState(): void {
    window.history.back();
  }
}

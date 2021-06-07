import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBookCase } from 'app/shared/model/book-case.model';

@Component({
  selector: 'jhi-book-case-detail',
  templateUrl: './book-case-detail.component.html',
})
export class BookCaseDetailComponent implements OnInit {
  bookCase: IBookCase | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookCase }) => (this.bookCase = bookCase));
  }

  previousState(): void {
    window.history.back();
  }
}

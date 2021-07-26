import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBookTitle } from 'app/shared/model/book-title.model';
import { BookTitleService } from './book-title.service';

@Component({
  selector: 'jhi-book-title-detail',
  templateUrl: './book-title-detail.component.html',
})
export class BookTitleDetailComponent implements OnInit {
  bookTitle: IBookTitle | null = null;
  urlImage: any;

  constructor(protected activatedRoute: ActivatedRoute, protected bookTitleService: BookTitleService) {}

  ngOnInit(): void {
    this.bookTitleService.find(this.activatedRoute.snapshot.params.id).subscribe(res => {
      this.bookTitle = res.body;
      this.urlImage = res.headers.get('urlImage');
    });
  }

  previousState(): void {
    window.history.back();
  }
}

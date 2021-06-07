import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBookCategory } from 'app/shared/model/book-category.model';
import { BookCategoryService } from './book-category.service';

@Component({
  templateUrl: './book-category-delete-dialog.component.html',
})
export class BookCategoryDeleteDialogComponent {
  bookCategory?: IBookCategory;

  constructor(
    protected bookCategoryService: BookCategoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookCategoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bookCategoryListModification');
      this.activeModal.close();
    });
  }
}

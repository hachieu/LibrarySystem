import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBorrowBook } from 'app/shared/model/borrow-book.model';
import { BorrowBookService } from './borrow-book.service';

@Component({
  templateUrl: './borrow-book-delete-dialog.component.html',
})
export class BorrowBookDeleteDialogComponent {
  borrowBook?: IBorrowBook;

  constructor(
    protected borrowBookService: BorrowBookService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.borrowBookService.delete(id).subscribe(() => {
      this.eventManager.broadcast('borrowBookListModification');
      this.activeModal.close();
    });
  }
}

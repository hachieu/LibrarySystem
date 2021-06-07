import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBookTitle } from 'app/shared/model/book-title.model';
import { BookTitleService } from './book-title.service';

@Component({
  templateUrl: './book-title-delete-dialog.component.html',
})
export class BookTitleDeleteDialogComponent {
  bookTitle?: IBookTitle;

  constructor(protected bookTitleService: BookTitleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookTitleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bookTitleListModification');
      this.activeModal.close();
    });
  }
}

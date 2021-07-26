import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBookCase } from 'app/shared/model/book-case.model';
import { BookCaseService } from './book-case.service';

@Component({
  templateUrl: './book-case-delete-dialog.component.html',
})
export class BookCaseDeleteDialogComponent {
  bookCase?: IBookCase;

  constructor(protected bookCaseService: BookCaseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bookCaseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('bookCaseListModification');
      this.activeModal.close();
    });
  }
}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LibaraySystemSharedModule } from 'app/shared/shared.module';
import { BorrowBookComponent } from './borrow-book.component';
import { BorrowBookDetailComponent } from './borrow-book-detail.component';
import { BorrowBookUpdateComponent } from './borrow-book-update.component';
import { BorrowBookDeleteDialogComponent } from './borrow-book-delete-dialog.component';
import { borrowBookRoute } from './borrow-book.route';

@NgModule({
  imports: [LibaraySystemSharedModule, RouterModule.forChild(borrowBookRoute)],
  declarations: [BorrowBookComponent, BorrowBookDetailComponent, BorrowBookUpdateComponent, BorrowBookDeleteDialogComponent],
  entryComponents: [BorrowBookDeleteDialogComponent],
})
export class LibaraySystemBorrowBookModule {}

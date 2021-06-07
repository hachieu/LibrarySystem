import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LibaraySystemSharedModule } from 'app/shared/shared.module';
import { BookCaseComponent } from './book-case.component';
import { BookCaseDetailComponent } from './book-case-detail.component';
import { BookCaseUpdateComponent } from './book-case-update.component';
import { BookCaseDeleteDialogComponent } from './book-case-delete-dialog.component';
import { bookCaseRoute } from './book-case.route';

@NgModule({
  imports: [LibaraySystemSharedModule, RouterModule.forChild(bookCaseRoute)],
  declarations: [BookCaseComponent, BookCaseDetailComponent, BookCaseUpdateComponent, BookCaseDeleteDialogComponent],
  entryComponents: [BookCaseDeleteDialogComponent],
})
export class LibaraySystemBookCaseModule {}

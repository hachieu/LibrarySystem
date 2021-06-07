import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LibaraySystemSharedModule } from 'app/shared/shared.module';
import { BookTitleComponent } from './book-title.component';
import { BookTitleDetailComponent } from './book-title-detail.component';
import { BookTitleUpdateComponent } from './book-title-update.component';
import { BookTitleDeleteDialogComponent } from './book-title-delete-dialog.component';
import { bookTitleRoute } from './book-title.route';

@NgModule({
  imports: [LibaraySystemSharedModule, RouterModule.forChild(bookTitleRoute)],
  declarations: [BookTitleComponent, BookTitleDetailComponent, BookTitleUpdateComponent, BookTitleDeleteDialogComponent],
  entryComponents: [BookTitleDeleteDialogComponent],
})
export class LibaraySystemBookTitleModule {}

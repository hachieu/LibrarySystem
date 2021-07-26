import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LibaraySystemSharedModule } from 'app/shared/shared.module';
import { BookCategoryComponent } from './book-category.component';
import { BookCategoryDetailComponent } from './book-category-detail.component';
import { BookCategoryUpdateComponent } from './book-category-update.component';
import { BookCategoryDeleteDialogComponent } from './book-category-delete-dialog.component';
import { bookCategoryRoute } from './book-category.route';

@NgModule({
  imports: [LibaraySystemSharedModule, RouterModule.forChild(bookCategoryRoute)],
  declarations: [BookCategoryComponent, BookCategoryDetailComponent, BookCategoryUpdateComponent, BookCategoryDeleteDialogComponent],
  entryComponents: [BookCategoryDeleteDialogComponent],
})
export class LibaraySystemBookCategoryModule {}

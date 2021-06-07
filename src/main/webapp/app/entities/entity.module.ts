import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'book-category',
        loadChildren: () => import('./book-category/book-category.module').then(m => m.LibaraySystemBookCategoryModule),
      },
      {
        path: 'book-case',
        loadChildren: () => import('./book-case/book-case.module').then(m => m.LibaraySystemBookCaseModule),
      },
      {
        path: 'book-title',
        loadChildren: () => import('./book-title/book-title.module').then(m => m.LibaraySystemBookTitleModule),
      },
      {
        path: 'card',
        loadChildren: () => import('./card/card.module').then(m => m.LibaraySystemCardModule),
      },
      {
        path: 'borrow-book',
        loadChildren: () => import('./borrow-book/borrow-book.module').then(m => m.LibaraySystemBorrowBookModule),
      },
      {
        path: 'book',
        loadChildren: () => import('./book/book.module').then(m => m.LibaraySystemBookModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class LibaraySystemEntityModule {}

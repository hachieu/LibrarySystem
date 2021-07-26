import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBookCategory, BookCategory } from 'app/shared/model/book-category.model';
import { BookCategoryService } from './book-category.service';
import { BookCategoryComponent } from './book-category.component';
import { BookCategoryDetailComponent } from './book-category-detail.component';
import { BookCategoryUpdateComponent } from './book-category-update.component';

@Injectable({ providedIn: 'root' })
export class BookCategoryResolve implements Resolve<IBookCategory> {
  constructor(private service: BookCategoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBookCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((bookCategory: HttpResponse<BookCategory>) => {
          if (bookCategory.body) {
            return of(bookCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BookCategory());
  }
}

export const bookCategoryRoute: Routes = [
  {
    path: '',
    component: BookCategoryComponent,
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'libaraySystemApp.bookCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BookCategoryDetailComponent,
    resolve: {
      bookCategory: BookCategoryResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BookCategoryUpdateComponent,
    resolve: {
      bookCategory: BookCategoryResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BookCategoryUpdateComponent,
    resolve: {
      bookCategory: BookCategoryResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

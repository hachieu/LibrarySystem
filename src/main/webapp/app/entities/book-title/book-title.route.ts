import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBookTitle, BookTitle } from 'app/shared/model/book-title.model';
import { BookTitleService } from './book-title.service';
import { BookTitleComponent } from './book-title.component';
import { BookTitleDetailComponent } from './book-title-detail.component';
import { BookTitleUpdateComponent } from './book-title-update.component';
import { TopBookTitleComponent } from './top-book-title/top-book-title.component';
import { JhiResolvePagingParams } from 'ng-jhipster';

@Injectable({ providedIn: 'root' })
export class BookTitleResolve implements Resolve<IBookTitle> {
  constructor(private service: BookTitleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBookTitle> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((bookTitle: HttpResponse<BookTitle>) => {
          if (bookTitle.body) {
            return of(bookTitle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BookTitle());
  }
}

export const bookTitleRoute: Routes = [
  {
    path: '',
    component: BookTitleComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'libaraySystemApp.bookTitle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BookTitleDetailComponent,
    resolve: {
      bookTitle: BookTitleResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookTitle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BookTitleUpdateComponent,
    resolve: {
      bookTitle: BookTitleResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookTitle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BookTitleUpdateComponent,
    resolve: {
      bookTitle: BookTitleResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookTitle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'top',
    component: TopBookTitleComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'libaraySystemApp.bookTitle.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

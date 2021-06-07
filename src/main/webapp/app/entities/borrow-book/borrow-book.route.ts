import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBorrowBook, BorrowBook } from 'app/shared/model/borrow-book.model';
import { BorrowBookService } from './borrow-book.service';
import { BorrowBookComponent } from './borrow-book.component';
import { BorrowBookDetailComponent } from './borrow-book-detail.component';
import { BorrowBookUpdateComponent } from './borrow-book-update.component';

@Injectable({ providedIn: 'root' })
export class BorrowBookResolve implements Resolve<IBorrowBook> {
  constructor(private service: BorrowBookService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBorrowBook> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((borrowBook: HttpResponse<BorrowBook>) => {
          if (borrowBook.body) {
            return of(borrowBook.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BorrowBook());
  }
}

export const borrowBookRoute: Routes = [
  {
    path: '',
    component: BorrowBookComponent,
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'libaraySystemApp.borrowBook.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BorrowBookDetailComponent,
    resolve: {
      borrowBook: BorrowBookResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.borrowBook.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BorrowBookUpdateComponent,
    resolve: {
      borrowBook: BorrowBookResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.borrowBook.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BorrowBookUpdateComponent,
    resolve: {
      borrowBook: BorrowBookResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.borrowBook.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

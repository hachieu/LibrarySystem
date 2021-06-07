import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IBookCase, BookCase } from 'app/shared/model/book-case.model';
import { BookCaseService } from './book-case.service';
import { BookCaseComponent } from './book-case.component';
import { BookCaseDetailComponent } from './book-case-detail.component';
import { BookCaseUpdateComponent } from './book-case-update.component';

@Injectable({ providedIn: 'root' })
export class BookCaseResolve implements Resolve<IBookCase> {
  constructor(private service: BookCaseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBookCase> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((bookCase: HttpResponse<BookCase>) => {
          if (bookCase.body) {
            return of(bookCase.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BookCase());
  }
}

export const bookCaseRoute: Routes = [
  {
    path: '',
    component: BookCaseComponent,
    data: {
      authorities: [Authority.ADMIN],
      defaultSort: 'id,asc',
      pageTitle: 'libaraySystemApp.bookCase.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BookCaseDetailComponent,
    resolve: {
      bookCase: BookCaseResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookCase.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BookCaseUpdateComponent,
    resolve: {
      bookCase: BookCaseResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookCase.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BookCaseUpdateComponent,
    resolve: {
      bookCase: BookCaseResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'libaraySystemApp.bookCase.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

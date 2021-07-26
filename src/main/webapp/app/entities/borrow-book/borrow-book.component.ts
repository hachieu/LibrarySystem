import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { BorrowBook, IBorrowBook } from 'app/shared/model/borrow-book.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BorrowBookService } from './borrow-book.service';
import { BorrowBookDeleteDialogComponent } from './borrow-book-delete-dialog.component';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'jhi-borrow-book',
  templateUrl: './borrow-book.component.html',
})
export class BorrowBookComponent implements OnInit, OnDestroy {
  borrowBooks?: IBorrowBook[];
  eventSubscriber?: Subscription;
  totalItems!: number;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  previousPage: any;
  reverse: any;
  links: any;

  dateUpdate:any;

  constructor(
    protected borrowBookService: BorrowBookService,
    protected activatedRoute: ActivatedRoute,
    protected eventManager: JhiEventManager,
    protected jhiAlertService: JhiAlertService,
    protected modalService: NgbModal,
    protected router: Router,
    protected parseLinks: JhiParseLinks,
    private datepipe: DatePipe) {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  loadData(page?: number): void {
    const pageToLoad: number = page || this.page || 1;
    this.page = pageToLoad;

    this.borrowBookService.findByDateUpdate({
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
      dateUpdate: this.dateUpdate
    })
      .subscribe(
        (res: HttpResponse<IBorrowBook[]>) => {
          this.onSuccess(res.body || [], res.headers);
        }
      );

    this.loadPage(this.page);
  }

  ngOnInit(): void {
    this.dateUpdate = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
    this.loadData(1);
    this.registerChangeInBorrowBooks();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  registerChangeInBorrowBooks(): void {
    this.eventSubscriber = this.eventManager.subscribe('borrowBookListModification', () => this.loadData(1));
  }

  delete(borrowBook: IBorrowBook): void {
    const modalRef = this.modalService.open(BorrowBookDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.borrowBook = borrowBook;
  }

  trackId(index: number, item: BorrowBook): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition(): void {
    this.router.navigate(['/book-title'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc'),
      },
    });
    this.loadData(this.page);
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IBorrowBook[], headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') || '');
    this.totalItems = parseInt(headers.get('X-Total-Count') || '', 10);
    this.borrowBooks = data;
  }
}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBook } from 'app/shared/model/book.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BookService } from './book.service';
import { BookDeleteDialogComponent } from './book-delete-dialog.component';
import * as fileSaver from 'file-saver'; 

@Component({
  selector: 'jhi-book',
  templateUrl: './book.component.html',
})
export class BookComponent implements OnInit, OnDestroy {
  books?: IBook[];
  eventSubscriber?: Subscription;
  totalItems!: number;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  previousPage: any;
  reverse: any;
  links: any;

  bookBarCode = '';
  borrowed = '';
  notBorrow = '';

  constructor(
    protected bookService: BookService,
    protected activatedRoute: ActivatedRoute,
    protected eventManager: JhiEventManager,
    protected jhiAlertService: JhiAlertService,
    protected modalService: NgbModal,
    protected router: Router,
    protected parseLinks: JhiParseLinks) {
    this.activatedRoute.data.subscribe(data => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.reverse = data.pagingParams.ascending;
      this.predicate = data.pagingParams.predicate;
    });
  }

  ngOnInit(): void {
    this.loadData(1);
    this.registerChangeInBooks();
  }

  loadData(page?: number): void {
    const pageToLoad: number = page || this.page || 1;
    this.page = pageToLoad;

    if(this.borrowed && !this.notBorrow) {
      this.borrowed = '1';
      this.notBorrow = '';
    } else if(this.notBorrow && !this.borrowed) {
      this.notBorrow = '0';
      this.borrowed = '';
    } else {
      this.borrowed = '';
      this.notBorrow = '';
    }

    this.bookService.query({
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
      bookBarCode: this.bookBarCode,
      borrowed: this.borrowed,
      notBorrow: this.notBorrow
    })
    .subscribe(
      (res: HttpResponse<IBook[]>) => this.onSuccess(res.body || [], res.headers),
    );

    this.loadPage(this.page);
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBook): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBooks(): void {
    this.eventSubscriber = this.eventManager.subscribe('bookListModification', () => this.loadData(1));
  }

  delete(book: IBook): void {
    const modalRef = this.modalService.open(BookDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.book = book;
  }

  loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition(): void {
    this.router.navigate(['/book'], {
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

  protected onSuccess(data: IBook[], headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') || '');
    this.totalItems = parseInt(headers.get('X-Total-Count') || '', 10);
    this.books = data;
  }

  saveFile(data: any, filename?: string): void {
    const blob = new Blob([data], { type: 'text/csv; charset=utf-8' });
    fileSaver.saveAs(blob, filename);
  }

  export(): void {
    if(this.borrowed && !this.notBorrow) {
      this.borrowed = '1';
      this.notBorrow = '';
    } else if(this.notBorrow && !this.borrowed) {
      this.notBorrow = '0';
      this.borrowed = '';
    }

    this.bookService.export({
      bookBarCode: this.bookBarCode,
      borrowed: this.borrowed,
      notBorrow: this.notBorrow
    }).subscribe(
      response => {
        const filename = response.headers.get("Allow");
        this.saveFile(response.body, filename);
      }
    );
  }
}

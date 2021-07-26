import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { BookTitle, IBookTitle } from 'app/shared/model/book-title.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BookTitleService } from './book-title.service';
import { BookTitleDeleteDialogComponent } from './book-title-delete-dialog.component';

@Component({
  selector: 'jhi-book-title',
  templateUrl: './book-title.component.html',
})
export class BookTitleComponent implements OnInit, OnDestroy {
  bookTitles?: IBookTitle[];
  eventSubscriber?: Subscription;
  totalItems!: number;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  previousPage: any;
  reverse: any;
  links: any;

  urlImage: any;
  bookTitleName = '';

  constructor(
    protected bookTitleService: BookTitleService,
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
    this.registerChangeInBookTitles();
  }

  loadData(page?: number): void {
    const pageToLoad: number = page || this.page || 1;
    this.page = pageToLoad;

    this.bookTitleService.query({
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
      bookTitleName: this.bookTitleName
    })
      .subscribe(
        (res: HttpResponse<IBookTitle[]>) => {
          this.onSuccess(res.body || [], res.headers);
          this.urlImage = res.headers.get('urlImage');
        }
      );

    this.loadPage(this.page);
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  registerChangeInBookTitles(): void {
    this.eventSubscriber = this.eventManager.subscribe('bookTitleListModification', () => this.loadData(1));
  }

  delete(bookTitle: IBookTitle): void {
    const modalRef = this.modalService.open(BookTitleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bookTitle = bookTitle;
  }

  trackId(index: number, item: BookTitle): number {
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

  protected onSuccess(data: IBookTitle[], headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') || '');
    this.totalItems = parseInt(headers.get('X-Total-Count') || '', 10);
    this.bookTitles = data;
  }

}

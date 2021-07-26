import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { BookTitle, IBookTitle } from 'app/shared/model/book-title.model';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { BookTitleService } from '../book-title.service';

@Component({
  selector: 'jhi-top-book-title',
  templateUrl: './top-book-title.component.html',
  styleUrls: ['./top-book-title.component.scss']
})
export class TopBookTitleComponent implements OnInit {
  bookTitles?: IBookTitle[];
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
    protected modalService: NgbModal,
    protected router: Router,
    protected parseLinks: JhiParseLinks ) {
      this.activatedRoute.data.subscribe(data => {
        this.page = data.pagingParams.page;
        this.previousPage = data.pagingParams.page;
        this.reverse = data.pagingParams.ascending;
        this.predicate = data.pagingParams.predicate;
      });
    }

  ngOnInit(): void {
    this.loadData(1);
  }

  loadData(page?: number): void {
    const pageToLoad: number = page || this.page || 1;
    this.page = pageToLoad;

    this.bookTitleService.findByTop({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        bookTitleName: this.bookTitleName,
      })
      .subscribe(
        (res: HttpResponse<IBookTitle[]>) => {
          this.onSuccess(res.body || [], res.headers);
          this.urlImage = res.headers.get('urlImage');
        }
      );

      this.loadPage(this.page);
  }

  trackId(index: number, item: BookTitle): number {
    return item.id!;
  }

  loadPage(page: number): void {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition(): void {
    this.router.navigate(['/book-title/top'], {
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

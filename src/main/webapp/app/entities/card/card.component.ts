import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Card, ICard } from 'app/shared/model/card.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CardService } from './card.service';
import { CardDeleteDialogComponent } from './card-delete-dialog.component';

@Component({
  selector: 'jhi-card',
  templateUrl: './card.component.html',
})
export class CardComponent implements OnInit, OnDestroy {
  cards?: ICard[];
  eventSubscriber?: Subscription;
  totalItems!: number;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  previousPage: any;
  reverse: any;
  links: any;

  fullName = '';

  constructor(
    protected cardService: CardService,
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

  loadData(page?: number): void {
    const pageToLoad: number = page || this.page || 1;
    this.page = pageToLoad;

    this.cardService.findByFullName({
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
      fullName: this.fullName
    })
      .subscribe(
        (res: HttpResponse<ICard[]>) => {
          this.onSuccess(res.body || [], res.headers);
        }
      );

    this.loadPage(this.page);
  }

  ngOnInit(): void {
    this.loadData(1);
    this.registerChangeInCards();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  registerChangeInCards(): void {
    this.eventSubscriber = this.eventManager.subscribe('cardListModification', () => this.loadData(1));
  }

  delete(card: ICard): void {
    const modalRef = this.modalService.open(CardDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.card = card;
  }

  trackId(index: number, item: Card): number {
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
    this.router.navigate(['/card'], {
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

  protected onSuccess(data: ICard[], headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') || '');
    this.totalItems = parseInt(headers.get('X-Total-Count') || '', 10);
    this.cards = data;
  }
}

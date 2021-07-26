import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LibaraySystemTestModule } from '../../../test.module';
import { BorrowBookDetailComponent } from 'app/entities/borrow-book/borrow-book-detail.component';
import { BorrowBook } from 'app/shared/model/borrow-book.model';

describe('Component Tests', () => {
  describe('BorrowBook Management Detail Component', () => {
    let comp: BorrowBookDetailComponent;
    let fixture: ComponentFixture<BorrowBookDetailComponent>;
    const route = ({ data: of({ borrowBook: new BorrowBook(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibaraySystemTestModule],
        declarations: [BorrowBookDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BorrowBookDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BorrowBookDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load borrowBook on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.borrowBook).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

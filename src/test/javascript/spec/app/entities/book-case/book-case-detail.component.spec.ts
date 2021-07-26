import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LibaraySystemTestModule } from '../../../test.module';
import { BookCaseDetailComponent } from 'app/entities/book-case/book-case-detail.component';
import { BookCase } from 'app/shared/model/book-case.model';

describe('Component Tests', () => {
  describe('BookCase Management Detail Component', () => {
    let comp: BookCaseDetailComponent;
    let fixture: ComponentFixture<BookCaseDetailComponent>;
    const route = ({ data: of({ bookCase: new BookCase(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibaraySystemTestModule],
        declarations: [BookCaseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BookCaseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BookCaseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bookCase on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bookCase).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

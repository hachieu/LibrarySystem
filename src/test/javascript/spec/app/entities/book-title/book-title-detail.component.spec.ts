import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LibaraySystemTestModule } from '../../../test.module';
import { BookTitleDetailComponent } from 'app/entities/book-title/book-title-detail.component';
import { BookTitle } from 'app/shared/model/book-title.model';

describe('Component Tests', () => {
  describe('BookTitle Management Detail Component', () => {
    let comp: BookTitleDetailComponent;
    let fixture: ComponentFixture<BookTitleDetailComponent>;
    const route = ({ data: of({ bookTitle: new BookTitle(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibaraySystemTestModule],
        declarations: [BookTitleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BookTitleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BookTitleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bookTitle on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bookTitle).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

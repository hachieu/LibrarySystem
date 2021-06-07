import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LibaraySystemTestModule } from '../../../test.module';
import { BookCategoryDetailComponent } from 'app/entities/book-category/book-category-detail.component';
import { BookCategory } from 'app/shared/model/book-category.model';

describe('Component Tests', () => {
  describe('BookCategory Management Detail Component', () => {
    let comp: BookCategoryDetailComponent;
    let fixture: ComponentFixture<BookCategoryDetailComponent>;
    const route = ({ data: of({ bookCategory: new BookCategory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibaraySystemTestModule],
        declarations: [BookCategoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(BookCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BookCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bookCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bookCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

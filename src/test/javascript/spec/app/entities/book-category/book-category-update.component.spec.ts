import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LibaraySystemTestModule } from '../../../test.module';
import { BookCategoryUpdateComponent } from 'app/entities/book-category/book-category-update.component';
import { BookCategoryService } from 'app/entities/book-category/book-category.service';
import { BookCategory } from 'app/shared/model/book-category.model';

describe('Component Tests', () => {
  describe('BookCategory Management Update Component', () => {
    let comp: BookCategoryUpdateComponent;
    let fixture: ComponentFixture<BookCategoryUpdateComponent>;
    let service: BookCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibaraySystemTestModule],
        declarations: [BookCategoryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(BookCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BookCategoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BookCategoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BookCategory(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new BookCategory();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

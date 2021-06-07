import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LibaraySystemTestModule } from '../../../test.module';
import { BookTitleUpdateComponent } from 'app/entities/book-title/book-title-update.component';
import { BookTitleService } from 'app/entities/book-title/book-title.service';
import { BookTitle } from 'app/shared/model/book-title.model';

describe('Component Tests', () => {
  describe('BookTitle Management Update Component', () => {
    let comp: BookTitleUpdateComponent;
    let fixture: ComponentFixture<BookTitleUpdateComponent>;
    let service: BookTitleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibaraySystemTestModule],
        declarations: [BookTitleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(BookTitleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BookTitleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BookTitleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BookTitle(123);
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
        const entity = new BookTitle();
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

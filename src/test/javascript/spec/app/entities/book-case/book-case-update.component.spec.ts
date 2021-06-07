import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LibaraySystemTestModule } from '../../../test.module';
import { BookCaseUpdateComponent } from 'app/entities/book-case/book-case-update.component';
import { BookCaseService } from 'app/entities/book-case/book-case.service';
import { BookCase } from 'app/shared/model/book-case.model';

describe('Component Tests', () => {
  describe('BookCase Management Update Component', () => {
    let comp: BookCaseUpdateComponent;
    let fixture: ComponentFixture<BookCaseUpdateComponent>;
    let service: BookCaseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibaraySystemTestModule],
        declarations: [BookCaseUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(BookCaseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BookCaseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BookCaseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BookCase(123);
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
        const entity = new BookCase();
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

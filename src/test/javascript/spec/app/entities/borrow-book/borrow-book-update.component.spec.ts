import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { LibaraySystemTestModule } from '../../../test.module';
import { BorrowBookUpdateComponent } from 'app/entities/borrow-book/borrow-book-update.component';
import { BorrowBookService } from 'app/entities/borrow-book/borrow-book.service';
import { BorrowBook } from 'app/shared/model/borrow-book.model';

describe('Component Tests', () => {
  describe('BorrowBook Management Update Component', () => {
    let comp: BorrowBookUpdateComponent;
    let fixture: ComponentFixture<BorrowBookUpdateComponent>;
    let service: BorrowBookService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [LibaraySystemTestModule],
        declarations: [BorrowBookUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(BorrowBookUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BorrowBookUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BorrowBookService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BorrowBook(123);
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
        const entity = new BorrowBook();
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

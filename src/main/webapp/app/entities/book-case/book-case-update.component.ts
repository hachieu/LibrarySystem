import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBookCase, BookCase } from 'app/shared/model/book-case.model';
import { BookCaseService } from './book-case.service';

@Component({
  selector: 'jhi-book-case-update',
  templateUrl: './book-case-update.component.html',
})
export class BookCaseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    bookCaseName: [null, [Validators.required]],
  });

  constructor(protected bookCaseService: BookCaseService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookCase }) => {
      this.updateForm(bookCase);
    });
  }

  updateForm(bookCase: IBookCase): void {
    this.editForm.patchValue({
      id: bookCase.id,
      bookCaseName: bookCase.bookCaseName,
      dateCreate: bookCase.dateCreate,
      dateUpdate: bookCase.dateUpdate,
      userCreate: bookCase.userCreate,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookCase = this.createFromForm();
    if (bookCase.id !== undefined) {
      this.subscribeToSaveResponse(this.bookCaseService.update(bookCase));
    } else {
      this.subscribeToSaveResponse(this.bookCaseService.create(bookCase));
    }
  }

  private createFromForm(): IBookCase {
    return {
      ...new BookCase(),
      id: this.editForm.get(['id'])!.value,
      bookCaseName: this.editForm.get(['bookCaseName'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookCase>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}

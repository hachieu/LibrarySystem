import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBookCategory, BookCategory } from 'app/shared/model/book-category.model';
import { BookCategoryService } from './book-category.service';

@Component({
  selector: 'jhi-book-category-update',
  templateUrl: './book-category-update.component.html',
})
export class BookCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    bookCategoryName: [null, [Validators.required]],
  });

  constructor(protected bookCategoryService: BookCategoryService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookCategory }) => {
      this.updateForm(bookCategory);
    });
  }

  updateForm(bookCategory: IBookCategory): void {
    this.editForm.patchValue({
      id: bookCategory.id,
      bookCategoryName: bookCategory.bookCategoryName,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookCategory = this.createFromForm();
    if (bookCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.bookCategoryService.update(bookCategory));
    } else {
      this.subscribeToSaveResponse(this.bookCategoryService.create(bookCategory));
    }
  }

  private createFromForm(): IBookCategory {
    return {
      ...new BookCategory(),
      id: this.editForm.get(['id'])!.value,
      bookCategoryName: this.editForm.get(['bookCategoryName'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookCategory>>): void {
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

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBookTitle, BookTitle } from 'app/shared/model/book-title.model';
import { BookTitleService } from './book-title.service';
import { IBookCategory } from 'app/shared/model/book-category.model';
import { BookCategoryService } from 'app/entities/book-category/book-category.service';

@Component({
  selector: 'jhi-book-title-update',
  templateUrl: './book-title-update.component.html',
})
export class BookTitleUpdateComponent implements OnInit {
  isSaving = false;
  bookcategories: IBookCategory[] = [];
  currentFile?: File;
  imagePath: any;
  imgURL: any;
  checkImge?: boolean;

  editForm = this.fb.group({
    id: [],
    bookTitleName: [null, [Validators.required]],
    author: [null, [Validators.required]],
    publicationDate: [null, [Validators.required]],
    page: [null, [Validators.required]],
    priceOfBook: [null, [Validators.required]],
    prireOfBorrow: [null, [Validators.required]],
    file: [null, [Validators.required]],
    bookCategory: [null, [Validators.required]],
  });

  constructor(
    protected bookTitleService: BookTitleService,
    protected bookCategoryService: BookCategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookTitle }) => {
      this.updateForm(bookTitle);

      this.bookCategoryService.query().subscribe((res: HttpResponse<IBookCategory[]>) => (this.bookcategories = res.body || []));
    });
  }

  updateForm(bookTitle: IBookTitle): void {
    this.editForm.patchValue({
      id: bookTitle.id,
      bookTitleName: bookTitle.bookTitleName,
      author: bookTitle.author,
      publicationDate: bookTitle.publicationDate,
      page: bookTitle.page,
      priceOfBook: bookTitle.priceOfBook,
      prireOfBorrow: bookTitle.prireOfBorrow,
      image: bookTitle.image,
      dateCreate: bookTitle.dateCreate,
      dateUpdate: bookTitle.dateUpdate,
      userCreate: bookTitle.userCreate,
      bookCategory: bookTitle.bookCategory,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookTitle = this.createFromForm();

    const formData: FormData = new FormData();

    if (this.currentFile) {
      formData.append('file', this.currentFile);
    }
    formData.append('bookTitle', JSON.stringify(bookTitle));

    if (bookTitle.id !== undefined) {
      this.subscribeToSaveResponse(this.bookTitleService.update(formData));
    } else {
      this.subscribeToSaveResponse(this.bookTitleService.create(formData));
    }
  }

  private createFromForm(): IBookTitle {
    return {
      ...new BookTitle(),
      id: this.editForm.get(['id'])!.value,
      bookTitleName: this.editForm.get(['bookTitleName'])!.value,
      author: this.editForm.get(['author'])!.value,
      publicationDate: this.editForm.get(['publicationDate'])!.value,
      page: this.editForm.get(['page'])!.value,
      priceOfBook: this.editForm.get(['priceOfBook'])!.value,
      prireOfBorrow: this.editForm.get(['prireOfBorrow'])!.value,
      image: this.currentFile?.name,
      bookCategory: this.editForm.get(['bookCategory'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookTitle>>): void {
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

  trackById(index: number, item: IBookCategory): any {
    return item.id;
  }

  selectFile(event: any): void {
    this.currentFile = event.target.files.item(0);

    if (this.currentFile) {
      const sizeFile = this.currentFile.size;

      if (sizeFile > 2000000) {
        this.checkImge = true;
      } else {
        const reader = new FileReader();

        this.imagePath = this.currentFile;
        reader.readAsDataURL(this.currentFile);
        reader.onload = () => {
          this.imgURL = reader.result;
        };
      }
    }
  }
}

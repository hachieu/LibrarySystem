import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { BookCaseService } from 'app/entities/book-case/book-case.service';
import { BookTitleService } from 'app/entities/book-title/book-title.service';
import { IBookCase } from 'app/shared/model/book-case.model';
import { IBookTitle } from 'app/shared/model/book-title.model';
import { Book, IBook } from 'app/shared/model/book.model';
import { Observable } from 'rxjs';
import { BookService } from '../book.service';

type SelectableEntity = IBookTitle | IBookCase;

@Component({
  selector: 'jhi-import-book',
  templateUrl: './import-book.component.html',
  styleUrls: ['./import-book.component.scss']
})
export class ImportBookComponent implements OnInit {
  isSaving = false;
  booktitles: IBookTitle[] = [];
  bookcases: IBookCase[] = [];
  currentFile?: File;

  editForm = this.fb.group({
    bookTitle: [null, [Validators.required]],
    bookCase: [null, [Validators.required]],
    file: [null, [Validators.required]],
  });

  constructor(
    protected bookService: BookService,
    protected bookTitleService: BookTitleService,
    protected bookCaseService: BookCaseService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.bookTitleService.findAll().subscribe((res: HttpResponse<IBookTitle[]>) => (this.booktitles = res.body || []));

    this.bookCaseService.query().subscribe((res: HttpResponse<IBookCase[]>) => (this.bookcases = res.body || []));
  }

  private createFromForm(): IBook {
    return {
      ...new Book(),
      bookTitle: this.editForm.get(['bookTitle'])!.value,
      bookCase: this.editForm.get(['bookCase'])!.value,
    };
  }

  save(): void {
    this.isSaving = true;
    const book = this.createFromForm();

    const formData: FormData = new FormData();

    if(this.currentFile) {
      formData.append('file', this.currentFile);
    }

    formData.append('bookTitle', JSON.stringify(book.bookTitle));
    formData.append('bookCase', JSON.stringify(book.bookCase));

    this.subscribeToSaveResponse(this.bookService.import(formData));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBook>>): void {
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

  previousState(): void {
    window.history.back();
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  chooseFile(event: any): void {
    this.currentFile = event.target.files.item(0);
  }

}

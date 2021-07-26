import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { DatePipe } from '@angular/common';

import { ICard, Card } from 'app/shared/model/card.model';
import { CardService } from './card.service';

@Component({
  selector: 'jhi-card-update',
  templateUrl: './card-update.component.html',
})
export class CardUpdateComponent implements OnInit {
  isSaving = false;
  date: any;
  cardCode:any;

  editForm = this.fb.group({
    id: [],
    codeCard: [],
    fullName: [null, [Validators.required]],
    gender: [null, [Validators.required]],
    identity: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required]],
    address: [null, [Validators.required]],
    dateOfBirth: [null, [Validators.required]],
  });

  constructor(
    protected cardService: CardService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ card }) => {
      this.updateForm(card);
    });
  }

  updateForm(card: ICard): void {
    this.editForm.patchValue({
      id: card.id,
      codeCard: card.codeCard,
      fullName: card.fullName,
      gender: card.gender,
      identity: card.identity,
      phoneNumber: card.phoneNumber,
      address: card.address,
      dateOfBirth: card.dateOfBirth,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const card = this.createFromForm();
    if (card.id !== undefined) {
      this.subscribeToSaveResponse(this.cardService.update(card));
    } else {
      this.subscribeToSaveResponse(this.cardService.create(card));
    }
  }

  private createFromForm(): ICard {
    this.cardCode = this.datePipe.transform(Date.now(), 'ddMMyyyy') + this.editForm.get(['identity'])!.value;
    return {
      ...new Card(),
      id: this.editForm.get(['id'])!.value,
      codeCard: this.cardCode,
      fullName: this.editForm.get(['fullName'])!.value,
      gender: this.editForm.get(['gender'])!.value,
      identity: this.editForm.get(['identity'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      address: this.editForm.get(['address'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICard>>): void {
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

import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { CardService } from 'app/entities/card/card.service';
import { ICard, Card } from 'app/shared/model/card.model';

describe('Service Tests', () => {
  describe('Card Service', () => {
    let injector: TestBed;
    let service: CardService;
    let httpMock: HttpTestingController;
    let elemDefault: ICard;
    let expectedResult: ICard | ICard[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CardService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Card(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            dateOfBirth: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Card', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            dateOfBirth: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreate: currentDate,
            dateUpdate: currentDate,
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.create(new Card()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Card', () => {
        const returnedFromService = Object.assign(
          {
            codeCard: 'BBBBBB',
            fullName: 'BBBBBB',
            gender: 'BBBBBB',
            identity: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            address: 'BBBBBB',
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            userCreate: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreate: currentDate,
            dateUpdate: currentDate,
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Card', () => {
        const returnedFromService = Object.assign(
          {
            codeCard: 'BBBBBB',
            fullName: 'BBBBBB',
            gender: 'BBBBBB',
            identity: 'BBBBBB',
            phoneNumber: 'BBBBBB',
            address: 'BBBBBB',
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            userCreate: 'BBBBBB',
            dateOfBirth: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreate: currentDate,
            dateUpdate: currentDate,
            dateOfBirth: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Card', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

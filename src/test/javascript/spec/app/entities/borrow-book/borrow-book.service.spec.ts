import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { BorrowBookService } from 'app/entities/borrow-book/borrow-book.service';
import { IBorrowBook, BorrowBook } from 'app/shared/model/borrow-book.model';

describe('Service Tests', () => {
  describe('BorrowBook Service', () => {
    let injector: TestBed;
    let service: BorrowBookService;
    let httpMock: HttpTestingController;
    let elemDefault: IBorrowBook;
    let expectedResult: IBorrowBook | IBorrowBook[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BorrowBookService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new BorrowBook(0, currentDate, currentDate, 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a BorrowBook', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreate: currentDate,
            dateUpdate: currentDate,
          },
          returnedFromService
        );

        service.create(new BorrowBook()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BorrowBook', () => {
        const returnedFromService = Object.assign(
          {
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            userCreate: 'BBBBBB',
            status: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreate: currentDate,
            dateUpdate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BorrowBook', () => {
        const returnedFromService = Object.assign(
          {
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            userCreate: 'BBBBBB',
            status: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreate: currentDate,
            dateUpdate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a BorrowBook', () => {
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

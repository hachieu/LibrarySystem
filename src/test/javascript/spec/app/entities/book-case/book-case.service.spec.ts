import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { BookCaseService } from 'app/entities/book-case/book-case.service';
import { IBookCase, BookCase } from 'app/shared/model/book-case.model';

describe('Service Tests', () => {
  describe('BookCase Service', () => {
    let injector: TestBed;
    let service: BookCaseService;
    let httpMock: HttpTestingController;
    let elemDefault: IBookCase;
    let expectedResult: IBookCase | IBookCase[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BookCaseService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new BookCase(0, 'AAAAAAA', currentDate, currentDate, 'AAAAAAA');
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

      it('should create a BookCase', () => {
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

        service.create(new BookCase()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BookCase', () => {
        const returnedFromService = Object.assign(
          {
            bookCaseName: 'BBBBBB',
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            userCreate: 'BBBBBB',
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

      it('should return a list of BookCase', () => {
        const returnedFromService = Object.assign(
          {
            bookCaseName: 'BBBBBB',
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            userCreate: 'BBBBBB',
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

      it('should delete a BookCase', () => {
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

import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { BookTitleService } from 'app/entities/book-title/book-title.service';
import { IBookTitle, BookTitle } from 'app/shared/model/book-title.model';

describe('Service Tests', () => {
  describe('BookTitle Service', () => {
    let injector: TestBed;
    let service: BookTitleService;
    let httpMock: HttpTestingController;
    let elemDefault: IBookTitle;
    let expectedResult: IBookTitle | IBookTitle[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BookTitleService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new BookTitle(0, 'AAAAAAA', 'AAAAAAA', currentDate, 0, 0, 0, 'AAAAAAA', currentDate, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            publicationDate: currentDate.format(DATE_FORMAT),
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

      it('should create a BookTitle', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            publicationDate: currentDate.format(DATE_FORMAT),
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            publicationDate: currentDate,
            dateCreate: currentDate,
            dateUpdate: currentDate,
          },
          returnedFromService
        );

        service.create(new BookTitle()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BookTitle', () => {
        const returnedFromService = Object.assign(
          {
            bookTitleName: 'BBBBBB',
            author: 'BBBBBB',
            publicationDate: currentDate.format(DATE_FORMAT),
            page: 1,
            priceOfBook: 1,
            prireOfBorrow: 1,
            image: 'BBBBBB',
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            userCreate: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            publicationDate: currentDate,
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

      it('should return a list of BookTitle', () => {
        const returnedFromService = Object.assign(
          {
            bookTitleName: 'BBBBBB',
            author: 'BBBBBB',
            publicationDate: currentDate.format(DATE_FORMAT),
            page: 1,
            priceOfBook: 1,
            prireOfBorrow: 1,
            image: 'BBBBBB',
            dateCreate: currentDate.format(DATE_FORMAT),
            dateUpdate: currentDate.format(DATE_FORMAT),
            userCreate: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            publicationDate: currentDate,
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

      it('should delete a BookTitle', () => {
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

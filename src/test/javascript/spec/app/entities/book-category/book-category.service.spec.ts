import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { BookCategoryService } from 'app/entities/book-category/book-category.service';
import { IBookCategory, BookCategory } from 'app/shared/model/book-category.model';

describe('Service Tests', () => {
  describe('BookCategory Service', () => {
    let injector: TestBed;
    let service: BookCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IBookCategory;
    let expectedResult: IBookCategory | IBookCategory[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BookCategoryService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new BookCategory(0, 'AAAAAAA', currentDate, currentDate, 'AAAAAAA');
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

      it('should create a BookCategory', () => {
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

        service.create(new BookCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BookCategory', () => {
        const returnedFromService = Object.assign(
          {
            bookCategoryName: 'BBBBBB',
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

      it('should return a list of BookCategory', () => {
        const returnedFromService = Object.assign(
          {
            bookCategoryName: 'BBBBBB',
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

      it('should delete a BookCategory', () => {
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

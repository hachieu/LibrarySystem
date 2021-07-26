package com.service.impl;

import com.service.BookService;
import com.domain.Book;
import com.domain.BookCase;
import com.domain.BookTitle;
import com.domain.BorrowBook;
import com.repository.BookRepository;
import com.repository.BorrowBookRepository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final BorrowBookRepository borrowBookRepository;

    public BookServiceImpl(BookRepository bookRepository, BorrowBookRepository borrowBookRepository) {
        this.bookRepository = bookRepository;
        this.borrowBookRepository = borrowBookRepository;
    }

    @Override
    public Book save(Book book) {
        log.debug("Request to save Book : {}", book);

        if (book.getId() == null) {
            book.setDateCreate(LocalDate.now());
            Optional<Book> bookBarCode = bookRepository.findByBookBarCode(book.getBookBarCode());

            if (bookBarCode.isPresent()) {
                return null;
            }
        } else {
            Optional<Book> BookCaseOptional = bookRepository.findById(book.getId());
            book.setDateCreate(BookCaseOptional.get().getDateCreate());
        }

        book.setDateUpdate(LocalDate.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        book.setUserCreate(authentication.getName());

        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return bookRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    public Book findByBookBarCodeAndStatus(String bookBarCode, Integer status) {
        Optional<Book> book = bookRepository.findByBookBarCodeAndStatus(bookBarCode, status);

        if (book.isPresent()) {
            return book.get();
        }

        return null;
    }

    @Override
    public List<Book> findByBorrowBook(Long id) {
        Optional<BorrowBook> borrowBook = borrowBookRepository.findById(id);
        if (borrowBook.isPresent()) {
            List<Book> books = bookRepository.findByBorrowBook(borrowBook.get());

            return books;
        }

        return null;
    }

    @Override
    public Page<Book> findByBookBarCode(Pageable pageable, String bookBarCode, String borrowed, String notBorrow) {
        Integer status = 0;
        if ((borrowed.isEmpty() && notBorrow.isEmpty()) || (!borrowed.isEmpty() && !notBorrow.isEmpty())) {
            return bookRepository.findByBookBarCodeContainsIgnoreCase(pageable, bookBarCode);
        } else if (!borrowed.isEmpty() && notBorrow.isEmpty()) {
            status = 1;
        } else if (!notBorrow.isEmpty() && borrowed.isEmpty()) {
            status = 0;
        }

        return bookRepository.findAllByBookBarCodeContainsIgnoreCaseAndStatus(pageable, bookBarCode, status);
    }

    @Override
    public List<Book> findByBookBarCode(String bookBarCode, String borrowed, String notBorrow) {
        Integer status = 0;
        if ((borrowed.isEmpty() && notBorrow.isEmpty()) || (!borrowed.isEmpty() && !notBorrow.isEmpty())) {
            return bookRepository.findByBookBarCodeContainsIgnoreCase(bookBarCode);
        } else if (!borrowed.isEmpty() && notBorrow.isEmpty()) {
            status = 1;
        } else if (!notBorrow.isEmpty() && borrowed.isEmpty()) {
            status = 0;
        }

        return bookRepository.findAllByBookBarCodeContainsIgnoreCaseAndStatus(bookBarCode, status);
    }

    @Override
    public ByteArrayInputStream exportToCSV(List<Book> books) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        CSVPrinter csvPrinter = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            csvPrinter = new CSVPrinter(outputStreamWriter, format);

            List<String> headers = Arrays.asList("Book BarCode", "Book Title", "Book Case", "Borrow Book",
                    "Date Create", "Date Update", "User Create");
            csvPrinter.printRecord(headers);

            for (Book book : books) {
                List<String> data = Arrays.asList(book.getBookBarCode(), book.getBookTitle().getBookTitleName(),
                        book.getBookCase().getBookCaseName(),
                        book.getBorrowBook() != null ? book.getBorrowBook().getCard().getFullName() : "",
                        String.valueOf(book.getDateCreate()), String.valueOf(book.getDateUpdate()),
                        book.getUserCreate());

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvPrinter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public boolean hasCSVFormat(MultipartFile file) {
        String type = "text/csv";
        String msExcel = "application/vnd.ms-excel";

        if (type.equals(file.getContentType()) || file.getContentType().equals(msExcel)) {
            return true;
        }

        return false;
    }

    @Override
    public List<Book> readFileCSV(InputStream inputStream, BookTitle bookTitle, BookCase bookCase) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Book> listBook = new ArrayList<>();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Book book = new Book(csvRecord.get(0), 0, LocalDate.now(), LocalDate.now(), authentication.getName(),
                        bookTitle, bookCase, null);

                listBook.add(book);
            }

            return listBook;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean importFileCSV(MultipartFile file, BookTitle bookTitle, BookCase bookCase) {
        try {
            List<Book> listBooks = this.readFileCSV(file.getInputStream(), bookTitle, bookCase);

            if (listBooks != null && this.hasCSVFormat(file)) {
                bookRepository.saveAll(listBooks);

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}

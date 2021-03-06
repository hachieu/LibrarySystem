package com.web.rest;

import com.domain.Book;
import com.domain.BookCase;
import com.domain.BookTitle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.BookService;
import com.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for managing {@link com.domain.Book}.
 */
@RestController
@RequestMapping("/api")
public class BookResource {

    private final Logger log = LoggerFactory.getLogger(BookResource.class);

    private static final String ENTITY_NAME = "book";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * {@code POST  /books} : Create a new book.
     *
     * @param book the book to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new book, or with status {@code 400 (Bad Request)} if the
     *         book has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to save Book : {}", book);
        if (book.getId() != null) {
            throw new BadRequestAlertException("A new book cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Book result = bookService.save(book);

        if (result == null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "bookFailed", "barcode"))
                    .body(null);
        }

        return ResponseEntity
                .created(new URI("/api/books/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /books} : Updates an existing book.
     *
     * @param book the book to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated book, or with status {@code 400 (Bad Request)} if the
     *         book is not valid, or with status {@code 500 (Internal Server Error)}
     *         if the book couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/books")
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book) throws URISyntaxException {
        log.debug("REST request to update Book : {}", book);
        if (book.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Book result = bookService.save(book);
        return ResponseEntity.ok()
                .headers(
                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, book.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /books} : get all the books.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of books in body.
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(Pageable pageable, String bookBarCode, String borrowed,
            String notBorrow) {

        log.debug("REST request to get a page of Books");
        Page<Book> page = bookService.findByBookBarCode(pageable, bookBarCode, borrowed, notBorrow);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /books/:id} : get the "id" book.
     *
     * @param id the id of the book to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the book, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        log.debug("REST request to get Book : {}", id);
        Optional<Book> book = bookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(book);
    }

    /**
     * {@code DELETE  /books/:id} : delete the "id" book.
     *
     * @param id the id of the book to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.debug("REST request to delete Book : {}", id);
        bookService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    /**
     * 
     * @param bookBarCode
     * @return
     */
    @GetMapping("/books/find-barcode/{bookBarCode}/{status}")
    public ResponseEntity<Book> findByBookBarCode(@PathVariable String bookBarCode, @PathVariable Integer status) {
        log.debug("REST request to get Book : {}", bookBarCode);

        Book book = bookService.findByBookBarCodeAndStatus(bookBarCode, status);

        if (book != null) {
            return ResponseEntity.ok().body(book);
        }

        return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "bookNull", "failed"))
                .body(null);
    }

    @GetMapping("/books/find-borrow/{id}")
    public ResponseEntity<List<Book>> findByBorrowBook(@PathVariable Long id) {
        try {
            List<Book> books = bookService.findByBorrowBook(id);

            return ResponseEntity.ok().body(books);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/books/export")
    public ResponseEntity<Resource> downloadFile(HttpServletResponse response, String bookBarCode, String borrowed,
            String notBorrow) {
        List<Book> listBooks = bookService.findByBookBarCode(bookBarCode, borrowed, notBorrow);

        if (listBooks != null) {
            String fileName = "Book_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
                    + ".csv";
            ByteArrayInputStream byteArrayInputStream = bookService.exportToCSV(listBooks);
            if (byteArrayInputStream != null) {
                InputStreamResource file = new InputStreamResource(byteArrayInputStream);

                response.setCharacterEncoding("UTF-8");
                response.setHeader(HttpHeaders.ALLOW, fileName);

                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                        .contentType(MediaType.parseMediaType("application/csv")).body(file);
            }
        }

        return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "fileNull", "failed"))
                .body(null);
    }

    @PostMapping("/books/import")
    public ResponseEntity<Void> importFile(@RequestParam(value = "file") MultipartFile file,
            @RequestParam("bookTitle") String bookTitle, @RequestParam("bookCase") String bookCase) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            BookTitle bookTitleData = mapper.readValue(bookTitle, BookTitle.class);
            BookCase bookCaseData = mapper.readValue(bookCase, BookCase.class);

            boolean result = bookService.importFileCSV(file, bookTitleData, bookCaseData);

            if (result) {
                return ResponseEntity.noContent().headers(HeaderUtil.createEntityCreationAlert(applicationName, true,
                        ENTITY_NAME, file.getOriginalFilename())).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "fileImport", "failed"))
                .body(null);
    }
}

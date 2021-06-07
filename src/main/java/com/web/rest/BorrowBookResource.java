package com.web.rest;

import com.domain.Book;
import com.domain.BorrowBook;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.BorrowBookService;
import com.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.domain.BorrowBook}.
 */
@RestController
@RequestMapping("/api")
public class BorrowBookResource {

    private final Logger log = LoggerFactory.getLogger(BorrowBookResource.class);

    private static final String ENTITY_NAME = "borrowBook";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BorrowBookService borrowBookService;

    public BorrowBookResource(BorrowBookService borrowBookService) {
        this.borrowBookService = borrowBookService;
    }

    /**
     * {@code POST  /borrow-books} : Create a new borrowBook.
     *
     * @param borrowBook the borrowBook to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new borrowBook, or with status {@code 400 (Bad Request)} if
     *         the borrowBook has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/borrow-books")
    public ResponseEntity<BorrowBook> createBorrowBook(@RequestParam("borrowBook") String borrowBook,
            @RequestParam("books") String books) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            BorrowBook borrowBookData = mapper.readValue(borrowBook, BorrowBook.class);
            List<Book> listBooks = mapper.readValue(books, new TypeReference<List<Book>>() {
            });

            if (borrowBookData.getId() != null) {
                throw new BadRequestAlertException("A new borrowBook cannot already have an ID", ENTITY_NAME,
                        "idexists");
            }

            BorrowBook result = borrowBookService.save(borrowBookData, listBooks);

            if(result != null) {
                return ResponseEntity
                    .created(new URI("/api/borrow-books/" + result.getId())).headers(HeaderUtil
                            .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "listBorrowNull", "failed"))
                .body(null);
    }

    /**
     * {@code PUT  /borrow-books} : Updates an existing borrowBook.
     *
     * @param borrowBook the borrowBook to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated borrowBook, or with status {@code 400 (Bad Request)} if
     *         the borrowBook is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the borrowBook couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/borrow-books")
    public ResponseEntity<BorrowBook> updateBorrowBook(@RequestParam("borrowBook") String borrowBook,
    @RequestParam("books") String books) {
        log.debug("REST request to update BorrowBook : {}", borrowBook);
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            BorrowBook borrowBookData = mapper.readValue(borrowBook, BorrowBook.class);
            List<Book> listBooks = mapper.readValue(books, new TypeReference<List<Book>>() {
            });

            if (borrowBookData.getId() == null) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
            BorrowBook result = borrowBookService.update(borrowBookData, listBooks);

            if(result != null) {
                return ResponseEntity.ok().headers(
                    HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, borrowBookData.getId().toString()))
                    .body(result);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "listreturnNull", "failed"))
                .body(null);
    }

    /**
     * {@code GET  /borrow-books} : get all the borrowBooks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of borrowBooks in body.
     */
    @GetMapping("/borrow-books")
    public ResponseEntity<List<BorrowBook>> getAllBorrowBooks(Pageable pageable) {
        log.debug("REST request to get a page of BorrowBooks");
        Page<BorrowBook> page = borrowBookService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /borrow-books/:id} : get the "id" borrowBook.
     *
     * @param id the id of the borrowBook to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the borrowBook, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/borrow-books/{id}")
    public ResponseEntity<BorrowBook> getBorrowBook(@PathVariable Long id) {
        log.debug("REST request to get BorrowBook : {}", id);
        Optional<BorrowBook> borrowBook = borrowBookService.findOne(id);
        return ResponseUtil.wrapOrNotFound(borrowBook);
    }

    /**
     * {@code DELETE  /borrow-books/:id} : delete the "id" borrowBook.
     *
     * @param id the id of the borrowBook to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/borrow-books/{id}")
    public ResponseEntity<Void> deleteBorrowBook(@PathVariable Long id) {
        log.debug("REST request to delete BorrowBook : {}", id);
        borrowBookService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }
}

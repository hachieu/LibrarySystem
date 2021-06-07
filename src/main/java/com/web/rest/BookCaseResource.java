package com.web.rest;

import com.domain.BookCase;
import com.service.BookCaseService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.domain.BookCase}.
 */
@RestController
@RequestMapping("/api")
public class BookCaseResource {

    private final Logger log = LoggerFactory.getLogger(BookCaseResource.class);

    private static final String ENTITY_NAME = "bookCase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookCaseService bookCaseService;

    public BookCaseResource(BookCaseService bookCaseService) {
        this.bookCaseService = bookCaseService;
    }

    /**
     * {@code POST  /book-cases} : Create a new bookCase.
     *
     * @param bookCase the bookCase to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookCase, or with status {@code 400 (Bad Request)} if the bookCase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-cases")
    public ResponseEntity<BookCase> createBookCase(@Valid @RequestBody BookCase bookCase) throws URISyntaxException {
        log.debug("REST request to save BookCase : {}", bookCase);
        if (bookCase.getId() != null) {
            throw new BadRequestAlertException("A new bookCase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookCase result = bookCaseService.save(bookCase);
        return ResponseEntity.created(new URI("/api/book-cases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-cases} : Updates an existing bookCase.
     *
     * @param bookCase the bookCase to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookCase,
     * or with status {@code 400 (Bad Request)} if the bookCase is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookCase couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-cases")
    public ResponseEntity<BookCase> updateBookCase(@Valid @RequestBody BookCase bookCase) throws URISyntaxException {
        log.debug("REST request to update BookCase : {}", bookCase);
        if (bookCase.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BookCase result = bookCaseService.save(bookCase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookCase.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /book-cases} : get all the bookCases.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookCases in body.
     */
    @GetMapping("/book-cases")
    public ResponseEntity<List<BookCase>> getAllBookCases(Pageable pageable) {
        log.debug("REST request to get a page of BookCases");
        Page<BookCase> page = bookCaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-cases/:id} : get the "id" bookCase.
     *
     * @param id the id of the bookCase to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookCase, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-cases/{id}")
    public ResponseEntity<BookCase> getBookCase(@PathVariable Long id) {
        log.debug("REST request to get BookCase : {}", id);
        Optional<BookCase> bookCase = bookCaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookCase);
    }

    /**
     * {@code DELETE  /book-cases/:id} : delete the "id" bookCase.
     *
     * @param id the id of the bookCase to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-cases/{id}")
    public ResponseEntity<Void> deleteBookCase(@PathVariable Long id) {
        log.debug("REST request to delete BookCase : {}", id);
        bookCaseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

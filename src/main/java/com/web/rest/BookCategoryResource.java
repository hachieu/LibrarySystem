package com.web.rest;

import com.domain.BookCategory;
import com.service.BookCategoryService;
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
 * REST controller for managing {@link com.domain.BookCategory}.
 */
@RestController
@RequestMapping("/api")
public class BookCategoryResource {

    private final Logger log = LoggerFactory.getLogger(BookCategoryResource.class);

    private static final String ENTITY_NAME = "bookCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookCategoryService bookCategoryService;

    public BookCategoryResource(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    /**
     * {@code POST  /book-categories} : Create a new bookCategory.
     *
     * @param bookCategory the bookCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookCategory, or with status {@code 400 (Bad Request)} if the bookCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-categories")
    public ResponseEntity<BookCategory> createBookCategory(@Valid @RequestBody BookCategory bookCategory) throws URISyntaxException {
        log.debug("REST request to save BookCategory : {}", bookCategory);
        if (bookCategory.getId() != null) {
            throw new BadRequestAlertException("A new bookCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookCategory result = bookCategoryService.save(bookCategory);
        return ResponseEntity.created(new URI("/api/book-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /book-categories} : Updates an existing bookCategory.
     *
     * @param bookCategory the bookCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookCategory,
     * or with status {@code 400 (Bad Request)} if the bookCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-categories")
    public ResponseEntity<BookCategory> updateBookCategory(@Valid @RequestBody BookCategory bookCategory) throws URISyntaxException {
        log.debug("REST request to update BookCategory : {}", bookCategory);
        if (bookCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BookCategory result = bookCategoryService.save(bookCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /book-categories} : get all the bookCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookCategories in body.
     */
    @GetMapping("/book-categories")
    public ResponseEntity<List<BookCategory>> getAllBookCategories(Pageable pageable) {
        log.debug("REST request to get a page of BookCategories");
        Page<BookCategory> page = bookCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /book-categories/:id} : get the "id" bookCategory.
     *
     * @param id the id of the bookCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-categories/{id}")
    public ResponseEntity<BookCategory> getBookCategory(@PathVariable Long id) {
        log.debug("REST request to get BookCategory : {}", id);
        Optional<BookCategory> bookCategory = bookCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookCategory);
    }

    /**
     * {@code DELETE  /book-categories/:id} : delete the "id" bookCategory.
     *
     * @param id the id of the bookCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-categories/{id}")
    public ResponseEntity<Void> deleteBookCategory(@PathVariable Long id) {
        log.debug("REST request to delete BookCategory : {}", id);
        bookCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

package com.web.rest;

import com.domain.BookTitle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.BookTitleService;
import com.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.domain.BookTitle}.
 */
@RestController
@RequestMapping("/api")
public class BookTitleResource {

    private final Logger log = LoggerFactory.getLogger(BookTitleResource.class);

    private static final String ENTITY_NAME = "bookTitle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Value("${folder.upload.urlres}")
    private String folderName;

    private final BookTitleService bookTitleService;

    public BookTitleResource(BookTitleService bookTitleService) {
        this.bookTitleService = bookTitleService;
    }

    /**
     * {@code POST  /book-titles} : Create a new bookTitle.
     *
     * @param bookTitle the bookTitle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new bookTitle, or with status {@code 400 (Bad Request)} if
     *         the bookTitle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/book-titles")
    public ResponseEntity<BookTitle> createBookTitle(@RequestParam("bookTitle") String bookTitle,
            @RequestParam(value = "file") MultipartFile file) {
        log.debug("REST request to save BookTitle : {}", bookTitle);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            BookTitle bookTitleData = mapper.readValue(bookTitle, BookTitle.class);

            if (bookTitleData.getId() != null) {
                throw new BadRequestAlertException("A new bookTitle cannot already have an ID", ENTITY_NAME,
                        "idexists");
            }
            BookTitle result = bookTitleService.save(bookTitleData, file);

            if (result != null) {
                return ResponseEntity.created(new URI("/api/book-titles/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "cannotSave", "failed"))
                .body(null);
    }

    /**
     * {@code PUT  /book-titles} : Updates an existing bookTitle.
     *
     * @param bookTitle the bookTitle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated bookTitle, or with status {@code 400 (Bad Request)} if
     *         the bookTitle is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the bookTitle couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/book-titles")
    public ResponseEntity<BookTitle> updateBookTitle(@RequestParam("bookTitle") String bookTitle,
            @RequestParam(value = "file") MultipartFile file) {
        log.debug("REST request to update BookTitle : {}", bookTitle);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            BookTitle bookTitleData = mapper.readValue(bookTitle, BookTitle.class);

            if (bookTitleData.getId() == null) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
            BookTitle result = bookTitleService.update(bookTitleData, file);
            if (result != null) {
                return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
                        ENTITY_NAME, result.getId().toString())).body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(applicationName, true, ENTITY_NAME, "cannotSave", "failed"))
                .body(null);
    }

    /**
     * {@code GET  /book-titles/:id} : get the "id" bookTitle.
     *
     * @param id the id of the bookTitle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the bookTitle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/book-titles/{id}")
    public ResponseEntity<BookTitle> getBookTitle(@PathVariable Long id) {
        log.debug("REST request to get BookTitle : {}", id);

        Optional<BookTitle> bookTitle = bookTitleService.findOne(id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("urlImage", folderName);

        return ResponseEntity.ok().headers(headers).body(bookTitle.get());
    }

    /**
     * {@code DELETE  /book-titles/:id} : delete the "id" bookTitle.
     *
     * @param id the id of the bookTitle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/book-titles/{id}")
    public ResponseEntity<Void> deleteBookTitle(@PathVariable Long id) {
        log.debug("REST request to delete BookTitle : {}", id);
        bookTitleService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    /**
     * 
     * @param pageable
     * @param bookTitleName
     * @return
     */
    @GetMapping("/book-titles/find-top")
    public ResponseEntity<List<BookTitle>> findByTop(Pageable pageable, String bookTitleName) {
        Page<BookTitle> page = bookTitleService.findByTop(pageable, bookTitleName);

        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        headers.add("urlImage", folderName);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * 
     * @param pageable
     * @param bookTitleName
     * @return
     */
    @GetMapping("/book-titles/search")
    public ResponseEntity<List<BookTitle>> findByBookTitleName(Pageable pageable, String bookTitleName) {
        Page<BookTitle> page = bookTitleService.findByBookTitleName(pageable, bookTitleName);

        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        headers.add("urlImage", folderName);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/book-titles")
    public ResponseEntity<List<BookTitle>> findAll(Pageable pageable) {
        Page<BookTitle> page = bookTitleService.findAll(pageable);

        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}

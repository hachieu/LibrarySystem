package com.web.rest;

import com.LibaraySystemApp;
import com.domain.BookCase;
import com.repository.BookCaseRepository;
import com.service.BookCaseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BookCaseResource} REST controller.
 */
@SpringBootTest(classes = LibaraySystemApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BookCaseResourceIT {

    private static final String DEFAULT_BOOK_CASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_CASE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_UPDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_CREATE = "AAAAAAAAAA";
    private static final String UPDATED_USER_CREATE = "BBBBBBBBBB";

    @Autowired
    private BookCaseRepository bookCaseRepository;

    @Autowired
    private BookCaseService bookCaseService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookCaseMockMvc;

    private BookCase bookCase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCase createEntity(EntityManager em) {
        BookCase bookCase = new BookCase()
            .bookCaseName(DEFAULT_BOOK_CASE_NAME)
            .dateCreate(DEFAULT_DATE_CREATE)
            .dateUpdate(DEFAULT_DATE_UPDATE)
            .userCreate(DEFAULT_USER_CREATE);
        return bookCase;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCase createUpdatedEntity(EntityManager em) {
        BookCase bookCase = new BookCase()
            .bookCaseName(UPDATED_BOOK_CASE_NAME)
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE);
        return bookCase;
    }

    @BeforeEach
    public void initTest() {
        bookCase = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookCase() throws Exception {
        int databaseSizeBeforeCreate = bookCaseRepository.findAll().size();
        // Create the BookCase
        restBookCaseMockMvc.perform(post("/api/book-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCase)))
            .andExpect(status().isCreated());

        // Validate the BookCase in the database
        List<BookCase> bookCaseList = bookCaseRepository.findAll();
        assertThat(bookCaseList).hasSize(databaseSizeBeforeCreate + 1);
        BookCase testBookCase = bookCaseList.get(bookCaseList.size() - 1);
        assertThat(testBookCase.getBookCaseName()).isEqualTo(DEFAULT_BOOK_CASE_NAME);
        assertThat(testBookCase.getDateCreate()).isEqualTo(DEFAULT_DATE_CREATE);
        assertThat(testBookCase.getDateUpdate()).isEqualTo(DEFAULT_DATE_UPDATE);
        assertThat(testBookCase.getUserCreate()).isEqualTo(DEFAULT_USER_CREATE);
    }

    @Test
    @Transactional
    public void createBookCaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookCaseRepository.findAll().size();

        // Create the BookCase with an existing ID
        bookCase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookCaseMockMvc.perform(post("/api/book-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCase)))
            .andExpect(status().isBadRequest());

        // Validate the BookCase in the database
        List<BookCase> bookCaseList = bookCaseRepository.findAll();
        assertThat(bookCaseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBookCaseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookCaseRepository.findAll().size();
        // set the field null
        bookCase.setBookCaseName(null);

        // Create the BookCase, which fails.


        restBookCaseMockMvc.perform(post("/api/book-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCase)))
            .andExpect(status().isBadRequest());

        List<BookCase> bookCaseList = bookCaseRepository.findAll();
        assertThat(bookCaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookCases() throws Exception {
        // Initialize the database
        bookCaseRepository.saveAndFlush(bookCase);

        // Get all the bookCaseList
        restBookCaseMockMvc.perform(get("/api/book-cases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookCase.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookCaseName").value(hasItem(DEFAULT_BOOK_CASE_NAME)))
            .andExpect(jsonPath("$.[*].dateCreate").value(hasItem(DEFAULT_DATE_CREATE.toString())))
            .andExpect(jsonPath("$.[*].dateUpdate").value(hasItem(DEFAULT_DATE_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].userCreate").value(hasItem(DEFAULT_USER_CREATE)));
    }
    
    @Test
    @Transactional
    public void getBookCase() throws Exception {
        // Initialize the database
        bookCaseRepository.saveAndFlush(bookCase);

        // Get the bookCase
        restBookCaseMockMvc.perform(get("/api/book-cases/{id}", bookCase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookCase.getId().intValue()))
            .andExpect(jsonPath("$.bookCaseName").value(DEFAULT_BOOK_CASE_NAME))
            .andExpect(jsonPath("$.dateCreate").value(DEFAULT_DATE_CREATE.toString()))
            .andExpect(jsonPath("$.dateUpdate").value(DEFAULT_DATE_UPDATE.toString()))
            .andExpect(jsonPath("$.userCreate").value(DEFAULT_USER_CREATE));
    }
    @Test
    @Transactional
    public void getNonExistingBookCase() throws Exception {
        // Get the bookCase
        restBookCaseMockMvc.perform(get("/api/book-cases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookCase() throws Exception {
        // Initialize the database
        bookCaseService.save(bookCase);

        int databaseSizeBeforeUpdate = bookCaseRepository.findAll().size();

        // Update the bookCase
        BookCase updatedBookCase = bookCaseRepository.findById(bookCase.getId()).get();
        // Disconnect from session so that the updates on updatedBookCase are not directly saved in db
        em.detach(updatedBookCase);
        updatedBookCase
            .bookCaseName(UPDATED_BOOK_CASE_NAME)
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE);

        restBookCaseMockMvc.perform(put("/api/book-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBookCase)))
            .andExpect(status().isOk());

        // Validate the BookCase in the database
        List<BookCase> bookCaseList = bookCaseRepository.findAll();
        assertThat(bookCaseList).hasSize(databaseSizeBeforeUpdate);
        BookCase testBookCase = bookCaseList.get(bookCaseList.size() - 1);
        assertThat(testBookCase.getBookCaseName()).isEqualTo(UPDATED_BOOK_CASE_NAME);
        assertThat(testBookCase.getDateCreate()).isEqualTo(UPDATED_DATE_CREATE);
        assertThat(testBookCase.getDateUpdate()).isEqualTo(UPDATED_DATE_UPDATE);
        assertThat(testBookCase.getUserCreate()).isEqualTo(UPDATED_USER_CREATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBookCase() throws Exception {
        int databaseSizeBeforeUpdate = bookCaseRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookCaseMockMvc.perform(put("/api/book-cases")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCase)))
            .andExpect(status().isBadRequest());

        // Validate the BookCase in the database
        List<BookCase> bookCaseList = bookCaseRepository.findAll();
        assertThat(bookCaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBookCase() throws Exception {
        // Initialize the database
        bookCaseService.save(bookCase);

        int databaseSizeBeforeDelete = bookCaseRepository.findAll().size();

        // Delete the bookCase
        restBookCaseMockMvc.perform(delete("/api/book-cases/{id}", bookCase.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookCase> bookCaseList = bookCaseRepository.findAll();
        assertThat(bookCaseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

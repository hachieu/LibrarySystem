package com.web.rest;

import com.LibrarySystemApp;
import com.domain.BookCategory;
import com.repository.BookCategoryRepository;
import com.service.BookCategoryService;

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
 * Integration tests for the {@link BookCategoryResource} REST controller.
 */
@SpringBootTest(classes = LibrarySystemApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BookCategoryResourceIT {

    private static final String DEFAULT_BOOK_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_CATEGORY_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_UPDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_CREATE = "AAAAAAAAAA";
    private static final String UPDATED_USER_CREATE = "BBBBBBBBBB";

    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private BookCategoryService bookCategoryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookCategoryMockMvc;

    private BookCategory bookCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCategory createEntity(EntityManager em) {
        BookCategory bookCategory = new BookCategory()
            .bookCategoryName(DEFAULT_BOOK_CATEGORY_NAME)
            .dateCreate(DEFAULT_DATE_CREATE)
            .dateUpdate(DEFAULT_DATE_UPDATE)
            .userCreate(DEFAULT_USER_CREATE);
        return bookCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookCategory createUpdatedEntity(EntityManager em) {
        BookCategory bookCategory = new BookCategory()
            .bookCategoryName(UPDATED_BOOK_CATEGORY_NAME)
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE);
        return bookCategory;
    }

    @BeforeEach
    public void initTest() {
        bookCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookCategory() throws Exception {
        int databaseSizeBeforeCreate = bookCategoryRepository.findAll().size();
        // Create the BookCategory
        restBookCategoryMockMvc.perform(post("/api/book-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCategory)))
            .andExpect(status().isCreated());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        BookCategory testBookCategory = bookCategoryList.get(bookCategoryList.size() - 1);
        assertThat(testBookCategory.getBookCategoryName()).isEqualTo(DEFAULT_BOOK_CATEGORY_NAME);
        assertThat(testBookCategory.getDateCreate()).isEqualTo(DEFAULT_DATE_CREATE);
        assertThat(testBookCategory.getDateUpdate()).isEqualTo(DEFAULT_DATE_UPDATE);
        assertThat(testBookCategory.getUserCreate()).isEqualTo(DEFAULT_USER_CREATE);
    }

    @Test
    @Transactional
    public void createBookCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookCategoryRepository.findAll().size();

        // Create the BookCategory with an existing ID
        bookCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookCategoryMockMvc.perform(post("/api/book-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCategory)))
            .andExpect(status().isBadRequest());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBookCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookCategoryRepository.findAll().size();
        // set the field null
        bookCategory.setBookCategoryName(null);

        // Create the BookCategory, which fails.


        restBookCategoryMockMvc.perform(post("/api/book-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCategory)))
            .andExpect(status().isBadRequest());

        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookCategories() throws Exception {
        // Initialize the database
        bookCategoryRepository.saveAndFlush(bookCategory);

        // Get all the bookCategoryList
        restBookCategoryMockMvc.perform(get("/api/book-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookCategoryName").value(hasItem(DEFAULT_BOOK_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].dateCreate").value(hasItem(DEFAULT_DATE_CREATE.toString())))
            .andExpect(jsonPath("$.[*].dateUpdate").value(hasItem(DEFAULT_DATE_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].userCreate").value(hasItem(DEFAULT_USER_CREATE)));
    }
    
    @Test
    @Transactional
    public void getBookCategory() throws Exception {
        // Initialize the database
        bookCategoryRepository.saveAndFlush(bookCategory);

        // Get the bookCategory
        restBookCategoryMockMvc.perform(get("/api/book-categories/{id}", bookCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookCategory.getId().intValue()))
            .andExpect(jsonPath("$.bookCategoryName").value(DEFAULT_BOOK_CATEGORY_NAME))
            .andExpect(jsonPath("$.dateCreate").value(DEFAULT_DATE_CREATE.toString()))
            .andExpect(jsonPath("$.dateUpdate").value(DEFAULT_DATE_UPDATE.toString()))
            .andExpect(jsonPath("$.userCreate").value(DEFAULT_USER_CREATE));
    }
    @Test
    @Transactional
    public void getNonExistingBookCategory() throws Exception {
        // Get the bookCategory
        restBookCategoryMockMvc.perform(get("/api/book-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookCategory() throws Exception {
        // Initialize the database
        bookCategoryService.save(bookCategory);

        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();

        // Update the bookCategory
        BookCategory updatedBookCategory = bookCategoryRepository.findById(bookCategory.getId()).get();
        // Disconnect from session so that the updates on updatedBookCategory are not directly saved in db
        em.detach(updatedBookCategory);
        updatedBookCategory
            .bookCategoryName(UPDATED_BOOK_CATEGORY_NAME)
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE);

        restBookCategoryMockMvc.perform(put("/api/book-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBookCategory)))
            .andExpect(status().isOk());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
        BookCategory testBookCategory = bookCategoryList.get(bookCategoryList.size() - 1);
        assertThat(testBookCategory.getBookCategoryName()).isEqualTo(UPDATED_BOOK_CATEGORY_NAME);
        assertThat(testBookCategory.getDateCreate()).isEqualTo(UPDATED_DATE_CREATE);
        assertThat(testBookCategory.getDateUpdate()).isEqualTo(UPDATED_DATE_UPDATE);
        assertThat(testBookCategory.getUserCreate()).isEqualTo(UPDATED_USER_CREATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBookCategory() throws Exception {
        int databaseSizeBeforeUpdate = bookCategoryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookCategoryMockMvc.perform(put("/api/book-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookCategory)))
            .andExpect(status().isBadRequest());

        // Validate the BookCategory in the database
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBookCategory() throws Exception {
        // Initialize the database
        bookCategoryService.save(bookCategory);

        int databaseSizeBeforeDelete = bookCategoryRepository.findAll().size();

        // Delete the bookCategory
        restBookCategoryMockMvc.perform(delete("/api/book-categories/{id}", bookCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookCategory> bookCategoryList = bookCategoryRepository.findAll();
        assertThat(bookCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

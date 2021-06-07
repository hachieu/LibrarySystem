package com.web.rest;

import com.LibaraySystemApp;
import com.domain.BorrowBook;
import com.repository.BorrowBookRepository;
import com.service.BorrowBookService;

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
 * Integration tests for the {@link BorrowBookResource} REST controller.
 */
@SpringBootTest(classes = LibaraySystemApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BorrowBookResourceIT {

    private static final LocalDate DEFAULT_DATE_CREATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_UPDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_CREATE = "AAAAAAAAAA";
    private static final String UPDATED_USER_CREATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private BorrowBookRepository borrowBookRepository;

    @Autowired
    private BorrowBookService borrowBookService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBorrowBookMockMvc;

    private BorrowBook borrowBook;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BorrowBook createEntity(EntityManager em) {
        BorrowBook borrowBook = new BorrowBook()
            .dateCreate(DEFAULT_DATE_CREATE)
            .dateUpdate(DEFAULT_DATE_UPDATE)
            .userCreate(DEFAULT_USER_CREATE)
            .status(DEFAULT_STATUS);
        return borrowBook;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BorrowBook createUpdatedEntity(EntityManager em) {
        BorrowBook borrowBook = new BorrowBook()
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE)
            .status(UPDATED_STATUS);
        return borrowBook;
    }

    @BeforeEach
    public void initTest() {
        borrowBook = createEntity(em);
    }

    @Test
    @Transactional
    public void createBorrowBook() throws Exception {
        int databaseSizeBeforeCreate = borrowBookRepository.findAll().size();
        // Create the BorrowBook
        restBorrowBookMockMvc.perform(post("/api/borrow-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(borrowBook)))
            .andExpect(status().isCreated());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeCreate + 1);
        BorrowBook testBorrowBook = borrowBookList.get(borrowBookList.size() - 1);
        assertThat(testBorrowBook.getDateCreate()).isEqualTo(DEFAULT_DATE_CREATE);
        assertThat(testBorrowBook.getDateUpdate()).isEqualTo(DEFAULT_DATE_UPDATE);
        assertThat(testBorrowBook.getUserCreate()).isEqualTo(DEFAULT_USER_CREATE);
        assertThat(testBorrowBook.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createBorrowBookWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = borrowBookRepository.findAll().size();

        // Create the BorrowBook with an existing ID
        borrowBook.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBorrowBookMockMvc.perform(post("/api/borrow-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(borrowBook)))
            .andExpect(status().isBadRequest());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBorrowBooks() throws Exception {
        // Initialize the database
        borrowBookRepository.saveAndFlush(borrowBook);

        // Get all the borrowBookList
        restBorrowBookMockMvc.perform(get("/api/borrow-books?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(borrowBook.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreate").value(hasItem(DEFAULT_DATE_CREATE.toString())))
            .andExpect(jsonPath("$.[*].dateUpdate").value(hasItem(DEFAULT_DATE_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].userCreate").value(hasItem(DEFAULT_USER_CREATE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getBorrowBook() throws Exception {
        // Initialize the database
        borrowBookRepository.saveAndFlush(borrowBook);

        // Get the borrowBook
        restBorrowBookMockMvc.perform(get("/api/borrow-books/{id}", borrowBook.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(borrowBook.getId().intValue()))
            .andExpect(jsonPath("$.dateCreate").value(DEFAULT_DATE_CREATE.toString()))
            .andExpect(jsonPath("$.dateUpdate").value(DEFAULT_DATE_UPDATE.toString()))
            .andExpect(jsonPath("$.userCreate").value(DEFAULT_USER_CREATE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }
    @Test
    @Transactional
    public void getNonExistingBorrowBook() throws Exception {
        // Get the borrowBook
        restBorrowBookMockMvc.perform(get("/api/borrow-books/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBorrowBook() throws Exception {
        // Initialize the database
        borrowBookService.save(borrowBook);

        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();

        // Update the borrowBook
        BorrowBook updatedBorrowBook = borrowBookRepository.findById(borrowBook.getId()).get();
        // Disconnect from session so that the updates on updatedBorrowBook are not directly saved in db
        em.detach(updatedBorrowBook);
        updatedBorrowBook
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE)
            .status(UPDATED_STATUS);

        restBorrowBookMockMvc.perform(put("/api/borrow-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBorrowBook)))
            .andExpect(status().isOk());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
        BorrowBook testBorrowBook = borrowBookList.get(borrowBookList.size() - 1);
        assertThat(testBorrowBook.getDateCreate()).isEqualTo(UPDATED_DATE_CREATE);
        assertThat(testBorrowBook.getDateUpdate()).isEqualTo(UPDATED_DATE_UPDATE);
        assertThat(testBorrowBook.getUserCreate()).isEqualTo(UPDATED_USER_CREATE);
        assertThat(testBorrowBook.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingBorrowBook() throws Exception {
        int databaseSizeBeforeUpdate = borrowBookRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBorrowBookMockMvc.perform(put("/api/borrow-books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(borrowBook)))
            .andExpect(status().isBadRequest());

        // Validate the BorrowBook in the database
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBorrowBook() throws Exception {
        // Initialize the database
        borrowBookService.save(borrowBook);

        int databaseSizeBeforeDelete = borrowBookRepository.findAll().size();

        // Delete the borrowBook
        restBorrowBookMockMvc.perform(delete("/api/borrow-books/{id}", borrowBook.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BorrowBook> borrowBookList = borrowBookRepository.findAll();
        assertThat(borrowBookList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

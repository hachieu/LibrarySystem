package com.web.rest;

import com.LibrarySystemApp;
import com.domain.BookTitle;
import com.repository.BookTitleRepository;
import com.service.BookTitleService;

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
 * Integration tests for the {@link BookTitleResource} REST controller.
 */
@SpringBootTest(classes = LibrarySystemApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BookTitleResourceIT {

    private static final String DEFAULT_BOOK_TITLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_TITLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AUTHOR = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLICATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer UPDATED_PAGE = 2;

    private static final Integer DEFAULT_PRICE_OF_BOOK = 1;
    private static final Integer UPDATED_PRICE_OF_BOOK = 2;

    private static final Integer DEFAULT_PRIRE_OF_BORROW = 1;
    private static final Integer UPDATED_PRIRE_OF_BORROW = 2;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_UPDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_CREATE = "AAAAAAAAAA";
    private static final String UPDATED_USER_CREATE = "BBBBBBBBBB";

    @Autowired
    private BookTitleRepository bookTitleRepository;

    @Autowired
    private BookTitleService bookTitleService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookTitleMockMvc;

    private BookTitle bookTitle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookTitle createEntity(EntityManager em) {
        BookTitle bookTitle = new BookTitle()
            .bookTitleName(DEFAULT_BOOK_TITLE_NAME)
            .author(DEFAULT_AUTHOR)
            .publicationDate(DEFAULT_PUBLICATION_DATE)
            .page(DEFAULT_PAGE)
            .priceOfBook(DEFAULT_PRICE_OF_BOOK)
            .prireOfBorrow(DEFAULT_PRIRE_OF_BORROW)
            .image(DEFAULT_IMAGE)
            .dateCreate(DEFAULT_DATE_CREATE)
            .dateUpdate(DEFAULT_DATE_UPDATE)
            .userCreate(DEFAULT_USER_CREATE);
        return bookTitle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookTitle createUpdatedEntity(EntityManager em) {
        BookTitle bookTitle = new BookTitle()
            .bookTitleName(UPDATED_BOOK_TITLE_NAME)
            .author(UPDATED_AUTHOR)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .page(UPDATED_PAGE)
            .priceOfBook(UPDATED_PRICE_OF_BOOK)
            .prireOfBorrow(UPDATED_PRIRE_OF_BORROW)
            .image(UPDATED_IMAGE)
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE);
        return bookTitle;
    }

    @BeforeEach
    public void initTest() {
        bookTitle = createEntity(em);
    }

    @Test
    @Transactional
    public void createBookTitle() throws Exception {
        int databaseSizeBeforeCreate = bookTitleRepository.findAll().size();
        // Create the BookTitle
        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isCreated());

        // Validate the BookTitle in the database
        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeCreate + 1);
        BookTitle testBookTitle = bookTitleList.get(bookTitleList.size() - 1);
        assertThat(testBookTitle.getBookTitleName()).isEqualTo(DEFAULT_BOOK_TITLE_NAME);
        assertThat(testBookTitle.getAuthor()).isEqualTo(DEFAULT_AUTHOR);
        assertThat(testBookTitle.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testBookTitle.getPage()).isEqualTo(DEFAULT_PAGE);
        assertThat(testBookTitle.getPriceOfBook()).isEqualTo(DEFAULT_PRICE_OF_BOOK);
        assertThat(testBookTitle.getPrireOfBorrow()).isEqualTo(DEFAULT_PRIRE_OF_BORROW);
        assertThat(testBookTitle.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBookTitle.getDateCreate()).isEqualTo(DEFAULT_DATE_CREATE);
        assertThat(testBookTitle.getDateUpdate()).isEqualTo(DEFAULT_DATE_UPDATE);
        assertThat(testBookTitle.getUserCreate()).isEqualTo(DEFAULT_USER_CREATE);
    }

    @Test
    @Transactional
    public void createBookTitleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookTitleRepository.findAll().size();

        // Create the BookTitle with an existing ID
        bookTitle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        // Validate the BookTitle in the database
        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkBookTitleNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookTitleRepository.findAll().size();
        // set the field null
        bookTitle.setBookTitleName(null);

        // Create the BookTitle, which fails.


        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAuthorIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookTitleRepository.findAll().size();
        // set the field null
        bookTitle.setAuthor(null);

        // Create the BookTitle, which fails.


        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublicationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookTitleRepository.findAll().size();
        // set the field null
        bookTitle.setPublicationDate(null);

        // Create the BookTitle, which fails.


        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPageIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookTitleRepository.findAll().size();
        // set the field null
        bookTitle.setPage(null);

        // Create the BookTitle, which fails.


        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceOfBookIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookTitleRepository.findAll().size();
        // set the field null
        bookTitle.setPriceOfBook(null);

        // Create the BookTitle, which fails.


        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrireOfBorrowIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookTitleRepository.findAll().size();
        // set the field null
        bookTitle.setPrireOfBorrow(null);

        // Create the BookTitle, which fails.


        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkImageIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookTitleRepository.findAll().size();
        // set the field null
        bookTitle.setImage(null);

        // Create the BookTitle, which fails.


        restBookTitleMockMvc.perform(post("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookTitles() throws Exception {
        // Initialize the database
        bookTitleRepository.saveAndFlush(bookTitle);

        // Get all the bookTitleList
        restBookTitleMockMvc.perform(get("/api/book-titles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookTitle.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookTitleName").value(hasItem(DEFAULT_BOOK_TITLE_NAME)))
            .andExpect(jsonPath("$.[*].author").value(hasItem(DEFAULT_AUTHOR)))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].page").value(hasItem(DEFAULT_PAGE)))
            .andExpect(jsonPath("$.[*].priceOfBook").value(hasItem(DEFAULT_PRICE_OF_BOOK)))
            .andExpect(jsonPath("$.[*].prireOfBorrow").value(hasItem(DEFAULT_PRIRE_OF_BORROW)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.[*].dateCreate").value(hasItem(DEFAULT_DATE_CREATE.toString())))
            .andExpect(jsonPath("$.[*].dateUpdate").value(hasItem(DEFAULT_DATE_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].userCreate").value(hasItem(DEFAULT_USER_CREATE)));
    }
    
    @Test
    @Transactional
    public void getBookTitle() throws Exception {
        // Initialize the database
        bookTitleRepository.saveAndFlush(bookTitle);

        // Get the bookTitle
        restBookTitleMockMvc.perform(get("/api/book-titles/{id}", bookTitle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookTitle.getId().intValue()))
            .andExpect(jsonPath("$.bookTitleName").value(DEFAULT_BOOK_TITLE_NAME))
            .andExpect(jsonPath("$.author").value(DEFAULT_AUTHOR))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()))
            .andExpect(jsonPath("$.page").value(DEFAULT_PAGE))
            .andExpect(jsonPath("$.priceOfBook").value(DEFAULT_PRICE_OF_BOOK))
            .andExpect(jsonPath("$.prireOfBorrow").value(DEFAULT_PRIRE_OF_BORROW))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE))
            .andExpect(jsonPath("$.dateCreate").value(DEFAULT_DATE_CREATE.toString()))
            .andExpect(jsonPath("$.dateUpdate").value(DEFAULT_DATE_UPDATE.toString()))
            .andExpect(jsonPath("$.userCreate").value(DEFAULT_USER_CREATE));
    }
    @Test
    @Transactional
    public void getNonExistingBookTitle() throws Exception {
        // Get the bookTitle
        restBookTitleMockMvc.perform(get("/api/book-titles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBookTitle() throws Exception {
        // Initialize the database
        bookTitleService.save(bookTitle);

        int databaseSizeBeforeUpdate = bookTitleRepository.findAll().size();

        // Update the bookTitle
        BookTitle updatedBookTitle = bookTitleRepository.findById(bookTitle.getId()).get();
        // Disconnect from session so that the updates on updatedBookTitle are not directly saved in db
        em.detach(updatedBookTitle);
        updatedBookTitle
            .bookTitleName(UPDATED_BOOK_TITLE_NAME)
            .author(UPDATED_AUTHOR)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .page(UPDATED_PAGE)
            .priceOfBook(UPDATED_PRICE_OF_BOOK)
            .prireOfBorrow(UPDATED_PRIRE_OF_BORROW)
            .image(UPDATED_IMAGE)
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE);

        restBookTitleMockMvc.perform(put("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedBookTitle)))
            .andExpect(status().isOk());

        // Validate the BookTitle in the database
        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeUpdate);
        BookTitle testBookTitle = bookTitleList.get(bookTitleList.size() - 1);
        assertThat(testBookTitle.getBookTitleName()).isEqualTo(UPDATED_BOOK_TITLE_NAME);
        assertThat(testBookTitle.getAuthor()).isEqualTo(UPDATED_AUTHOR);
        assertThat(testBookTitle.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testBookTitle.getPage()).isEqualTo(UPDATED_PAGE);
        assertThat(testBookTitle.getPriceOfBook()).isEqualTo(UPDATED_PRICE_OF_BOOK);
        assertThat(testBookTitle.getPrireOfBorrow()).isEqualTo(UPDATED_PRIRE_OF_BORROW);
        assertThat(testBookTitle.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBookTitle.getDateCreate()).isEqualTo(UPDATED_DATE_CREATE);
        assertThat(testBookTitle.getDateUpdate()).isEqualTo(UPDATED_DATE_UPDATE);
        assertThat(testBookTitle.getUserCreate()).isEqualTo(UPDATED_USER_CREATE);
    }

    @Test
    @Transactional
    public void updateNonExistingBookTitle() throws Exception {
        int databaseSizeBeforeUpdate = bookTitleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookTitleMockMvc.perform(put("/api/book-titles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookTitle)))
            .andExpect(status().isBadRequest());

        // Validate the BookTitle in the database
        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBookTitle() throws Exception {
        // Initialize the database
        bookTitleService.save(bookTitle);

        int databaseSizeBeforeDelete = bookTitleRepository.findAll().size();

        // Delete the bookTitle
        restBookTitleMockMvc.perform(delete("/api/book-titles/{id}", bookTitle.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookTitle> bookTitleList = bookTitleRepository.findAll();
        assertThat(bookTitleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.web.rest;

import com.LibrarySystemApp;
import com.domain.Card;
import com.repository.CardRepository;
import com.service.CardService;

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
 * Integration tests for the {@link CardResource} REST controller.
 */
@SpringBootTest(classes = LibrarySystemApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class CardResourceIT {

    private static final String DEFAULT_CODE_CARD = "AAAAAAAAAA";
    private static final String UPDATED_CODE_CARD = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTITY = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_UPDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_UPDATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_USER_CREATE = "AAAAAAAAAA";
    private static final String UPDATED_USER_CREATE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardMockMvc;

    private Card card;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {
        Card card = new Card()
            .codeCard(DEFAULT_CODE_CARD)
            .fullName(DEFAULT_FULL_NAME)
            .gender(DEFAULT_GENDER)
            .identity(DEFAULT_IDENTITY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .dateCreate(DEFAULT_DATE_CREATE)
            .dateUpdate(DEFAULT_DATE_UPDATE)
            .userCreate(DEFAULT_USER_CREATE)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH);
        return card;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createUpdatedEntity(EntityManager em) {
        Card card = new Card()
            .codeCard(UPDATED_CODE_CARD)
            .fullName(UPDATED_FULL_NAME)
            .gender(UPDATED_GENDER)
            .identity(UPDATED_IDENTITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH);
        return card;
    }

    @BeforeEach
    public void initTest() {
        card = createEntity(em);
    }

    @Test
    @Transactional
    public void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();
        // Create the Card
        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getCodeCard()).isEqualTo(DEFAULT_CODE_CARD);
        assertThat(testCard.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testCard.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testCard.getIdentity()).isEqualTo(DEFAULT_IDENTITY);
        assertThat(testCard.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testCard.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCard.getDateCreate()).isEqualTo(DEFAULT_DATE_CREATE);
        assertThat(testCard.getDateUpdate()).isEqualTo(DEFAULT_DATE_UPDATE);
        assertThat(testCard.getUserCreate()).isEqualTo(DEFAULT_USER_CREATE);
        assertThat(testCard.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void createCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card with an existing ID
        card.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeCardIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setCodeCard(null);

        // Create the Card, which fails.


        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setFullName(null);

        // Create the Card, which fails.


        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setGender(null);

        // Create the Card, which fails.


        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdentityIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setIdentity(null);

        // Create the Card, which fails.


        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setPhoneNumber(null);

        // Create the Card, which fails.


        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setAddress(null);

        // Create the Card, which fails.


        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardRepository.findAll().size();
        // set the field null
        card.setDateOfBirth(null);

        // Create the Card, which fails.


        restCardMockMvc.perform(post("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc.perform(get("/api/cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeCard").value(hasItem(DEFAULT_CODE_CARD)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].identity").value(hasItem(DEFAULT_IDENTITY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].dateCreate").value(hasItem(DEFAULT_DATE_CREATE.toString())))
            .andExpect(jsonPath("$.[*].dateUpdate").value(hasItem(DEFAULT_DATE_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].userCreate").value(hasItem(DEFAULT_USER_CREATE)))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())));
    }
    
    @Test
    @Transactional
    public void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.codeCard").value(DEFAULT_CODE_CARD))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.identity").value(DEFAULT_IDENTITY))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.dateCreate").value(DEFAULT_DATE_CREATE.toString()))
            .andExpect(jsonPath("$.dateUpdate").value(DEFAULT_DATE_UPDATE.toString()))
            .andExpect(jsonPath("$.userCreate").value(DEFAULT_USER_CREATE))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCard() throws Exception {
        // Initialize the database
        cardService.save(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findById(card.getId()).get();
        // Disconnect from session so that the updates on updatedCard are not directly saved in db
        em.detach(updatedCard);
        updatedCard
            .codeCard(UPDATED_CODE_CARD)
            .fullName(UPDATED_FULL_NAME)
            .gender(UPDATED_GENDER)
            .identity(UPDATED_IDENTITY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .dateCreate(UPDATED_DATE_CREATE)
            .dateUpdate(UPDATED_DATE_UPDATE)
            .userCreate(UPDATED_USER_CREATE)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH);

        restCardMockMvc.perform(put("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCard)))
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getCodeCard()).isEqualTo(UPDATED_CODE_CARD);
        assertThat(testCard.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testCard.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testCard.getIdentity()).isEqualTo(UPDATED_IDENTITY);
        assertThat(testCard.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testCard.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCard.getDateCreate()).isEqualTo(UPDATED_DATE_CREATE);
        assertThat(testCard.getDateUpdate()).isEqualTo(UPDATED_DATE_UPDATE);
        assertThat(testCard.getUserCreate()).isEqualTo(UPDATED_USER_CREATE);
        assertThat(testCard.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
    }

    @Test
    @Transactional
    public void updateNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardMockMvc.perform(put("/api/cards")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCard() throws Exception {
        // Initialize the database
        cardService.save(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Delete the card
        restCardMockMvc.perform(delete("/api/cards/{id}", card.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

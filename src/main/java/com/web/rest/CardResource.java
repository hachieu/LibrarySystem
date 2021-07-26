package com.web.rest;

import com.domain.Card;
import com.service.CardService;
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
 * REST controller for managing {@link com.domain.Card}.
 */
@RestController
@RequestMapping("/api")
public class CardResource {

    private final Logger log = LoggerFactory.getLogger(CardResource.class);

    private static final String ENTITY_NAME = "card";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardService cardService;

    public CardResource(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * {@code POST  /cards} : Create a new card.
     *
     * @param card the card to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new card, or with status {@code 400 (Bad Request)} if the
     *         card has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cards")
    public ResponseEntity<Card> createCard(@Valid @RequestBody Card card) throws URISyntaxException {
        log.debug("REST request to save Card : {}", card);
        if (card.getId() != null) {
            throw new BadRequestAlertException("A new card cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Card result = cardService.save(card);
        return ResponseEntity
                .created(new URI("/api/cards/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /cards} : Updates an existing card.
     *
     * @param card the card to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated card, or with status {@code 400 (Bad Request)} if the
     *         card is not valid, or with status {@code 500 (Internal Server Error)}
     *         if the card couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cards")
    public ResponseEntity<Card> updateCard(@Valid @RequestBody Card card) throws URISyntaxException {
        log.debug("REST request to update Card : {}", card);
        if (card.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Card result = cardService.save(card);
        return ResponseEntity.ok()
                .headers(
                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, card.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /cards} : get all the cards.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of cards in body.
     */
    @GetMapping("/cards")
    public ResponseEntity<List<Card>> getAllCards(Pageable pageable) {
        log.debug("REST request to get a page of Cards");
        Page<Card> page = cardService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cards/:id} : get the "id" card.
     *
     * @param id the id of the card to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the card, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cards/{id}")
    public ResponseEntity<Card> getCard(@PathVariable Long id) {
        log.debug("REST request to get Card : {}", id);
        Optional<Card> card = cardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(card);
    }

    /**
     * {@code DELETE  /cards/:id} : delete the "id" card.
     *
     * @param id the id of the card to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        log.debug("REST request to delete Card : {}", id);
        cardService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    @GetMapping("/cards/search")
    public ResponseEntity<List<Card>> findByFullName(Pageable pageable, String fullName) {
        log.debug("REST request to get a page of Cards");
        Page<Card> page = cardService.findByFullName(pageable, fullName);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}

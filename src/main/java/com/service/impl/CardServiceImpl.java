package com.service.impl;

import com.service.CardService;
import com.domain.Card;
import com.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Card}.
 */
@Service
@Transactional
public class CardServiceImpl implements CardService {

    private final Logger log = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card save(Card card) {
        log.debug("Request to save Card : {}", card);

        if (card.getId() == null) {
            card.setDateCreate(LocalDate.now());
        } else {
            Optional<Card> cardOptional = cardRepository.findById(card.getId());
            card.setDateCreate(cardOptional.get().getDateCreate());
        }

        card.setDateUpdate(LocalDate.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        card.setUserCreate(authentication.getName());

        return cardRepository.save(card);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Card> findAll(Pageable pageable) {
        log.debug("Request to get all Cards");
        return cardRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Card> findOne(Long id) {
        log.debug("Request to get Card : {}", id);
        return cardRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Card : {}", id);
        cardRepository.deleteById(id);
    }

    @Override
    public Page<Card> findByFullName(Pageable pageable, String fullName) {
        return cardRepository.findByFullNameContainsIgnoreCase(pageable, fullName);
    }
}

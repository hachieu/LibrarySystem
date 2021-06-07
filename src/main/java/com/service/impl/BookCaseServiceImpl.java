package com.service.impl;

import com.service.BookCaseService;
import com.domain.BookCase;
import com.repository.BookCaseRepository;
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
 * Service Implementation for managing {@link BookCase}.
 */
@Service
@Transactional
public class BookCaseServiceImpl implements BookCaseService {

    private final Logger log = LoggerFactory.getLogger(BookCaseServiceImpl.class);

    private final BookCaseRepository bookCaseRepository;

    public BookCaseServiceImpl(BookCaseRepository bookCaseRepository) {
        this.bookCaseRepository = bookCaseRepository;
    }

    @Override
    public BookCase save(BookCase bookCase) {
        log.debug("Request to save BookCase : {}", bookCase);

        if (bookCase.getId() ==  null) {
            bookCase.setDateCreate(LocalDate.now());
        } else {
            Optional<BookCase> BookCaseOptional= bookCaseRepository.findById(bookCase.getId());
            bookCase.setDateCreate(BookCaseOptional.get().getDateCreate());
        }

        bookCase.setDateUpdate(LocalDate.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        bookCase.setUserCreate(authentication.getName());

        return bookCaseRepository.save(bookCase);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookCase> findAll(Pageable pageable) {
        log.debug("Request to get all BookCases");
        return bookCaseRepository.findAll(pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BookCase> findOne(Long id) {
        log.debug("Request to get BookCase : {}", id);
        return bookCaseRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BookCase : {}", id);
        bookCaseRepository.deleteById(id);
    }
}

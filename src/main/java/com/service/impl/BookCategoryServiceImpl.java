package com.service.impl;

import com.service.BookCategoryService;
import com.domain.BookCategory;
import com.repository.BookCategoryRepository;
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
 * Service Implementation for managing {@link BookCategory}.
 */
@Service
@Transactional
public class BookCategoryServiceImpl implements BookCategoryService {

    private final Logger log = LoggerFactory.getLogger(BookCategoryServiceImpl.class);

    private final BookCategoryRepository bookCategoryRepository;

    public BookCategoryServiceImpl(BookCategoryRepository bookCategoryRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
    }

    @Override
    public BookCategory save(BookCategory bookCategory) {
        log.debug("Request to save BookCategory : {}", bookCategory);

        if (bookCategory.getId() == null) {
            bookCategory.setDateCreate(LocalDate.now());
        } else {
            Optional<BookCategory> bookCategoryOptional = bookCategoryRepository.findById(bookCategory.getId());
            bookCategory.setDateCreate(bookCategoryOptional.get().getDateCreate());
        }

        bookCategory.setDateUpdate(LocalDate.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        bookCategory.setUserCreate(authentication.getName());

        return bookCategoryRepository.save(bookCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookCategory> findAll(Pageable pageable) {
        log.debug("Request to get all BookCategories");
        return bookCategoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookCategory> findOne(Long id) {
        log.debug("Request to get BookCategory : {}", id);
        return bookCategoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BookCategory : {}", id);
        bookCategoryRepository.deleteById(id);
    }
}

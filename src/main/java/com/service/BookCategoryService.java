package com.service;

import com.domain.BookCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link BookCategory}.
 */
public interface BookCategoryService {

    /**
     * Save a bookCategory.
     *
     * @param bookCategory the entity to save.
     * @return the persisted entity.
     */
    BookCategory save(BookCategory bookCategory);

    /**
     * Get all the bookCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookCategory> findAll(Pageable pageable);


    /**
     * Get the "id" bookCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookCategory> findOne(Long id);

    /**
     * Delete the "id" bookCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

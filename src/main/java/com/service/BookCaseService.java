package com.service;

import com.domain.BookCase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link BookCase}.
 */
public interface BookCaseService {

    /**
     * Save a bookCase.
     *
     * @param bookCase the entity to save.
     * @return the persisted entity.
     */
    BookCase save(BookCase bookCase);

    /**
     * Get all the bookCases.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookCase> findAll(Pageable pageable);


    /**
     * Get the "id" bookCase.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookCase> findOne(Long id);

    /**
     * Delete the "id" bookCase.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

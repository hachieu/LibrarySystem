package com.service;

import com.domain.BookTitle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service Interface for managing {@link BookTitle}.
 */
public interface BookTitleService {

    /**
     * Save a bookTitle.
     *
     * @param bookTitle the entity to save.
     * @return the persisted entity.
     */
    BookTitle save(BookTitle bookTitle);

    /**
     * Get all the bookTitles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookTitle> findAll(Pageable pageable);


    /**
     * Get the "id" bookTitle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookTitle> findOne(Long id);

    /**
     * Delete the "id" bookTitle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    BookTitle save (BookTitle bookTitle, MultipartFile file);

    BookTitle update(BookTitle bookTitle, MultipartFile file);
}

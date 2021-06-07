package com.service;

import com.domain.Book;
import com.domain.BorrowBook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BorrowBook}.
 */
public interface BorrowBookService {

    /**
     * Save a borrowBook.
     *
     * @param borrowBook the entity to save.
     * @return the persisted entity.
     */
    BorrowBook save(BorrowBook borrowBook);

    /**
     * Get all the borrowBooks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BorrowBook> findAll(Pageable pageable);


    /**
     * Get the "id" borrowBook.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BorrowBook> findOne(Long id);

    /**
     * Delete the "id" borrowBook.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    BorrowBook save (BorrowBook borrowBook, List<Book> listBooks);

    BorrowBook update (BorrowBook borrowBook, List<Book> listBooks);
}

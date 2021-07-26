package com.service;

import com.domain.Book;
import com.domain.BookCase;
import com.domain.BookTitle;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Book}.
 */
public interface BookService {

    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    Book save(Book book);

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * Get the "id" book.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Book> findOne(Long id);

    /**
     * Delete the "id" book.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Book findByBookBarCodeAndStatus(String bookBarCode, Integer status);

    List<Book> findByBorrowBook(Long id);

    Page<Book> findByBookBarCode(Pageable pageable, String bookBarCode, String borrowed, String notBorrow);

    List<Book> findByBookBarCode(String bookBarCode, String borrowed, String notBorrow);

    ByteArrayInputStream exportToCSV(List<Book> books);

    boolean hasCSVFormat(MultipartFile file);

    List<Book> readFileCSV(InputStream inputStream, BookTitle bookTitle, BookCase bookCase);

    boolean importFileCSV(MultipartFile file, BookTitle bookTitle, BookCase bookCase);
}

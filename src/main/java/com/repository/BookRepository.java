package com.repository;

import java.util.List;
import java.util.Optional;

import com.domain.Book;
import com.domain.BookCase;
import com.domain.BookTitle;
import com.domain.BorrowBook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Book entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByBookBarCode(String bookBarCode);

    Optional<Book> findByBookBarCodeAndStatus(String bookBarCode, Integer status);

    Page<Book> findAllByBookBarCodeContainsIgnoreCaseAndStatus(Pageable pageable, String bookBarCode, Integer status);

    Page<Book> findByBookBarCodeContainsIgnoreCase(Pageable pageable, String bookBarCode);

    List<Book> findAllByBookBarCodeContainsIgnoreCaseAndStatus(String bookBarCode, Integer status);

    List<Book> findByBookBarCodeContainsIgnoreCase(String bookBarCode);

    List<Book> findByBorrowBook(BorrowBook borrowBook);
}

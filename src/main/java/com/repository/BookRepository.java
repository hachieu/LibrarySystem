package com.repository;

import java.util.List;
import java.util.Optional;

import com.domain.Book;

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

    @Query("FROM Book WHERE borrowBook.id = :id")
    List<Book> findByBorrowBook(@Param("id") Long id);
}

package com.repository;

import java.time.LocalDate;

import com.domain.BorrowBook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the BorrowBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {
    Page<BorrowBook> findByDateUpdate(Pageable pageable, LocalDate dateUpdate);
}

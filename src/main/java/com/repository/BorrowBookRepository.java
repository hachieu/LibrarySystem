package com.repository;

import com.domain.BorrowBook;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the BorrowBook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Long> {
}

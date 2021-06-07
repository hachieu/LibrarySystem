package com.repository;

import com.domain.BookCase;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the BookCase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookCaseRepository extends JpaRepository<BookCase, Long> {
}

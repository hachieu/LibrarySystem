package com.repository;

import com.domain.BookCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the BookCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
}

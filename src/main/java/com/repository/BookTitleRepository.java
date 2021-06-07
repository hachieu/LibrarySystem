package com.repository;

import com.domain.BookTitle;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the BookTitle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookTitleRepository extends JpaRepository<BookTitle, Long> {
    @Query("FROM BookTitle WHERE id = (SELECT MAX(id) FROM BookTitle)")
    BookTitle findByMaxId();
}

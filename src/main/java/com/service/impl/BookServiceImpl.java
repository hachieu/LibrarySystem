package com.service.impl;

import com.service.BookService;
import com.domain.Book;
import com.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        log.debug("Request to save Book : {}", book);

        if (book.getId() == null) {
            book.setDateCreate(LocalDate.now());
            Optional<Book> bookBarCode = bookRepository.findByBookBarCode(book.getBookBarCode());

            if (bookBarCode.isPresent()) {
                return null;
            }
        } else {
            Optional<Book> BookCaseOptional = bookRepository.findById(book.getId());
            book.setDateCreate(BookCaseOptional.get().getDateCreate());
        }

        book.setDateUpdate(LocalDate.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        book.setUserCreate(authentication.getName());

        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return bookRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }

    @Override
    public Book findByBookBarCodeAndStatus(String bookBarCode, Integer status) {
        Optional<Book> book = bookRepository.findByBookBarCodeAndStatus(bookBarCode, status);

        if (book.isPresent()) {
            return book.get();
        }

        return null;
    }

    @Override
    public List<Book> findByBorrowBook(Long id) {
        List<Book> books = bookRepository.findByBorrowBook(id);

        return books;
    }
}

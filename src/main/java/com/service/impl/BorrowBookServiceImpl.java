package com.service.impl;

import com.service.BorrowBookService;
import com.domain.Book;
import com.domain.BorrowBook;
import com.repository.BookRepository;
import com.repository.BorrowBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing {@link BorrowBook}.
 */
@Service
@Transactional
public class BorrowBookServiceImpl implements BorrowBookService {

    private final Logger log = LoggerFactory.getLogger(BorrowBookServiceImpl.class);

    private final BorrowBookRepository borrowBookRepository;
    private final BookRepository bookRepository;

    public BorrowBookServiceImpl(BorrowBookRepository borrowBookRepository, BookRepository bookRepository) {
        this.borrowBookRepository = borrowBookRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public BorrowBook save(BorrowBook borrowBook) {
        return borrowBookRepository.save(borrowBook);
    }

    @Override
    public BorrowBook save(BorrowBook borrowBook, List<Book> listBooks) {
        log.debug("Request to save BorrowBook : {}", borrowBook);

        borrowBook.setDateCreate(LocalDate.now());
        borrowBook.setDateUpdate(LocalDate.now());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        borrowBook.setUserCreate(authentication.getName());

        if (listBooks.size() > 0) {
            for (Book book : listBooks) {
                book.setStatus(1);
                book.setBorrowBook(borrowBook);
            }
            borrowBookRepository.save(borrowBook);
            bookRepository.saveAll(listBooks);

            return borrowBook;
        }

        return null;
    }

    @Override
    public BorrowBook update(BorrowBook borrowBook, List<Book> listBooks) {
        Optional<BorrowBook> bookCaseOptional = borrowBookRepository.findById(borrowBook.getId());

        if (bookCaseOptional.isPresent()) {
            borrowBook.setDateCreate(bookCaseOptional.get().getDateCreate());
            borrowBook.setDateUpdate(LocalDate.now());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            borrowBook.setUserCreate(authentication.getName());

            List<Book> books = bookRepository.findByBorrowBook(borrowBook);
            Map<String, Book> mapBook = new HashMap<>();
            List<Book> newList = new ArrayList<>();

            for (Book book : listBooks) {
                mapBook.put(book.getBookBarCode(), book);
            }

            if (listBooks.size() > 0 && books.size() == listBooks.size()) {
                for (Book book : books) {
                    Book dataBook = mapBook.get(book.getBookBarCode());

                    if (dataBook == null) {
                        return null;
                    }
                    dataBook.setStatus(0);
                    dataBook.setBorrowBook(null);

                    newList.add(dataBook);
                }

                borrowBookRepository.save(borrowBook);
                bookRepository.saveAll(newList);

                return borrowBook;
            }
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BorrowBook> findAll(Pageable pageable) {
        log.debug("Request to get all BorrowBooks");
        return borrowBookRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BorrowBook> findOne(Long id) {
        log.debug("Request to get BorrowBook : {}", id);
        return borrowBookRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BorrowBook : {}", id);
        borrowBookRepository.deleteById(id);
    }

    @Override
    public Page<BorrowBook> findByDateUpdate(Pageable pageable, String dateUpdate) {
        LocalDate localDateUpdate = LocalDate.parse(dateUpdate);

        return borrowBookRepository.findByDateUpdate(pageable, localDateUpdate);
    }
}

package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "book_bar_code", nullable = false)
    private String bookBarCode;

    @NotNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "date_update")
    private LocalDate dateUpdate;

    @Column(name = "user_create")
    private String userCreate;

    @ManyToOne
    @JsonIgnoreProperties(value = "bookTitles", allowSetters = true)
    private BookTitle bookTitle;

    @ManyToOne
    @JsonIgnoreProperties(value = "bookCases", allowSetters = true)
    private BookCase bookCase;

    @ManyToOne
    @JsonIgnoreProperties(value = "borrowBooks", allowSetters = true)
    private BorrowBook borrowBook;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookBarCode() {
        return bookBarCode;
    }

    public Book bookBarCode(String bookBarCode) {
        this.bookBarCode = bookBarCode;
        return this;
    }

    public void setBookBarCode(String bookBarCode) {
        this.bookBarCode = bookBarCode;
    }

    public Integer getStatus() {
        return status;
    }

    public Book status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public Book dateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateUpdate() {
        return dateUpdate;
    }

    public Book dateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
        return this;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public Book userCreate(String userCreate) {
        this.userCreate = userCreate;
        return this;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public BookTitle getBookTitle() {
        return bookTitle;
    }

    public Book bookTitle(BookTitle bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public void setBookTitle(BookTitle bookTitle) {
        this.bookTitle = bookTitle;
    }

    public BookCase getBookCase() {
        return bookCase;
    }

    public Book bookCase(BookCase bookCase) {
        this.bookCase = bookCase;
        return this;
    }

    public void setBookCase(BookCase bookCase) {
        this.bookCase = bookCase;
    }

    public BorrowBook getBorrowBook() {
        return borrowBook;
    }

    public Book borrowBook(BorrowBook borrowBook) {
        this.borrowBook = borrowBook;
        return this;
    }

    public void setBorrowBook(BorrowBook borrowBook) {
        this.borrowBook = borrowBook;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return id != null && id.equals(((Book) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", bookBarCode='" + getBookBarCode() + "'" +
            ", status=" + getStatus() +
            ", dateCreate='" + getDateCreate() + "'" +
            ", dateUpdate='" + getDateUpdate() + "'" +
            ", userCreate='" + getUserCreate() + "'" +
            "}";
    }
}

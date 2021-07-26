package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A BorrowBook.
 */
@Entity
@Table(name = "borrow_book")
public class BorrowBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "date_update")
    private LocalDate dateUpdate;

    @Column(name = "user_create")
    private String userCreate;

    @Column(name = "status")
    private Integer status;

    @OneToMany(mappedBy = "borrowBook")
    private Set<Book> borrowBooks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "cards", allowSetters = true)
    private Card card;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public BorrowBook dateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateUpdate() {
        return dateUpdate;
    }

    public BorrowBook dateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
        return this;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public BorrowBook userCreate(String userCreate) {
        this.userCreate = userCreate;
        return this;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Integer getStatus() {
        return status;
    }

    public BorrowBook status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<Book> getBorrowBooks() {
        return borrowBooks;
    }

    public BorrowBook borrowBooks(Set<Book> books) {
        this.borrowBooks = books;
        return this;
    }

    public BorrowBook addBorrowBook(Book book) {
        this.borrowBooks.add(book);
        book.setBorrowBook(this);
        return this;
    }

    public BorrowBook removeBorrowBook(Book book) {
        this.borrowBooks.remove(book);
        book.setBorrowBook(null);
        return this;
    }

    public void setBorrowBooks(Set<Book> books) {
        this.borrowBooks = books;
    }

    public Card getCard() {
        return card;
    }

    public BorrowBook card(Card card) {
        this.card = card;
        return this;
    }

    public void setCard(Card card) {
        this.card = card;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BorrowBook)) {
            return false;
        }
        return id != null && id.equals(((BorrowBook) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BorrowBook{" +
            "id=" + getId() +
            ", dateCreate='" + getDateCreate() + "'" +
            ", dateUpdate='" + getDateUpdate() + "'" +
            ", userCreate='" + getUserCreate() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}

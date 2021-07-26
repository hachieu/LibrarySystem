package com.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A BookCase.
 */
@Entity
@Table(name = "book_case")
public class BookCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "book_case_name", nullable = false)
    private String bookCaseName;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "date_update")
    private LocalDate dateUpdate;

    @Column(name = "user_create")
    private String userCreate;

    @OneToMany(mappedBy = "bookCase")
    private Set<Book> bookCases = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookCaseName() {
        return bookCaseName;
    }

    public BookCase bookCaseName(String bookCaseName) {
        this.bookCaseName = bookCaseName;
        return this;
    }

    public void setBookCaseName(String bookCaseName) {
        this.bookCaseName = bookCaseName;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public BookCase dateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateUpdate() {
        return dateUpdate;
    }

    public BookCase dateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
        return this;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public BookCase userCreate(String userCreate) {
        this.userCreate = userCreate;
        return this;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Set<Book> getBookCases() {
        return bookCases;
    }

    public BookCase bookCases(Set<Book> books) {
        this.bookCases = books;
        return this;
    }

    public BookCase addBookCase(Book book) {
        this.bookCases.add(book);
        book.setBookCase(this);
        return this;
    }

    public BookCase removeBookCase(Book book) {
        this.bookCases.remove(book);
        book.setBookCase(null);
        return this;
    }

    public void setBookCases(Set<Book> books) {
        this.bookCases = books;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookCase)) {
            return false;
        }
        return id != null && id.equals(((BookCase) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookCase{" +
            "id=" + getId() +
            ", bookCaseName='" + getBookCaseName() + "'" +
            ", dateCreate='" + getDateCreate() + "'" +
            ", dateUpdate='" + getDateUpdate() + "'" +
            ", userCreate='" + getUserCreate() + "'" +
            "}";
    }
}

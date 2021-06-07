package com.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A BookCategory.
 */
@Entity
@Table(name = "book_category")
public class BookCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "book_category_name", nullable = false)
    private String bookCategoryName;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "date_update")
    private LocalDate dateUpdate;

    @Column(name = "user_create")
    private String userCreate;

    @OneToMany(mappedBy = "bookCategory")
    private Set<BookTitle> bookCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookCategoryName() {
        return bookCategoryName;
    }

    public BookCategory bookCategoryName(String bookCategoryName) {
        this.bookCategoryName = bookCategoryName;
        return this;
    }

    public void setBookCategoryName(String bookCategoryName) {
        this.bookCategoryName = bookCategoryName;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public BookCategory dateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateUpdate() {
        return dateUpdate;
    }

    public BookCategory dateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
        return this;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public BookCategory userCreate(String userCreate) {
        this.userCreate = userCreate;
        return this;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Set<BookTitle> getBookCategories() {
        return bookCategories;
    }

    public BookCategory bookCategories(Set<BookTitle> bookTitles) {
        this.bookCategories = bookTitles;
        return this;
    }

    public BookCategory addBookCategory(BookTitle bookTitle) {
        this.bookCategories.add(bookTitle);
        bookTitle.setBookCategory(this);
        return this;
    }

    public BookCategory removeBookCategory(BookTitle bookTitle) {
        this.bookCategories.remove(bookTitle);
        bookTitle.setBookCategory(null);
        return this;
    }

    public void setBookCategories(Set<BookTitle> bookTitles) {
        this.bookCategories = bookTitles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookCategory)) {
            return false;
        }
        return id != null && id.equals(((BookCategory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookCategory{" +
            "id=" + getId() +
            ", bookCategoryName='" + getBookCategoryName() + "'" +
            ", dateCreate='" + getDateCreate() + "'" +
            ", dateUpdate='" + getDateUpdate() + "'" +
            ", userCreate='" + getUserCreate() + "'" +
            "}";
    }
}

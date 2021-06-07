package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A BookTitle.
 */
@Entity
@Table(name = "book_title")
public class BookTitle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "book_title_name", nullable = false)
    private String bookTitleName;

    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @NotNull
    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @NotNull
    @Column(name = "page", nullable = false)
    private Integer page;

    @NotNull
    @Column(name = "price_of_book", nullable = false)
    private Integer priceOfBook;

    @NotNull
    @Column(name = "prire_of_borrow", nullable = false)
    private Integer prireOfBorrow;

    @NotNull
    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "date_create")
    private LocalDate dateCreate;

    @Column(name = "date_update")
    private LocalDate dateUpdate;

    @Column(name = "user_create")
    private String userCreate;

    @OneToMany(mappedBy = "bookTitle")
    private Set<Book> bookTitles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "bookCategories", allowSetters = true)
    private BookCategory bookCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookTitleName() {
        return bookTitleName;
    }

    public BookTitle bookTitleName(String bookTitleName) {
        this.bookTitleName = bookTitleName;
        return this;
    }

    public void setBookTitleName(String bookTitleName) {
        this.bookTitleName = bookTitleName;
    }

    public String getAuthor() {
        return author;
    }

    public BookTitle author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public BookTitle publicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
        return this;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Integer getPage() {
        return page;
    }

    public BookTitle page(Integer page) {
        this.page = page;
        return this;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPriceOfBook() {
        return priceOfBook;
    }

    public BookTitle priceOfBook(Integer priceOfBook) {
        this.priceOfBook = priceOfBook;
        return this;
    }

    public void setPriceOfBook(Integer priceOfBook) {
        this.priceOfBook = priceOfBook;
    }

    public Integer getPrireOfBorrow() {
        return prireOfBorrow;
    }

    public BookTitle prireOfBorrow(Integer prireOfBorrow) {
        this.prireOfBorrow = prireOfBorrow;
        return this;
    }

    public void setPrireOfBorrow(Integer prireOfBorrow) {
        this.prireOfBorrow = prireOfBorrow;
    }

    public String getImage() {
        return image;
    }

    public BookTitle image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getDateCreate() {
        return dateCreate;
    }

    public BookTitle dateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
        return this;
    }

    public void setDateCreate(LocalDate dateCreate) {
        this.dateCreate = dateCreate;
    }

    public LocalDate getDateUpdate() {
        return dateUpdate;
    }

    public BookTitle dateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
        return this;
    }

    public void setDateUpdate(LocalDate dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public BookTitle userCreate(String userCreate) {
        this.userCreate = userCreate;
        return this;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public Set<Book> getBookTitles() {
        return bookTitles;
    }

    public BookTitle bookTitles(Set<Book> books) {
        this.bookTitles = books;
        return this;
    }

    public BookTitle addBookTitle(Book book) {
        this.bookTitles.add(book);
        book.setBookTitle(this);
        return this;
    }

    public BookTitle removeBookTitle(Book book) {
        this.bookTitles.remove(book);
        book.setBookTitle(null);
        return this;
    }

    public void setBookTitles(Set<Book> books) {
        this.bookTitles = books;
    }

    public BookCategory getBookCategory() {
        return bookCategory;
    }

    public BookTitle bookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
        return this;
    }

    public void setBookCategory(BookCategory bookCategory) {
        this.bookCategory = bookCategory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookTitle)) {
            return false;
        }
        return id != null && id.equals(((BookTitle) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookTitle{" +
            "id=" + getId() +
            ", bookTitleName='" + getBookTitleName() + "'" +
            ", author='" + getAuthor() + "'" +
            ", publicationDate='" + getPublicationDate() + "'" +
            ", page=" + getPage() +
            ", priceOfBook=" + getPriceOfBook() +
            ", prireOfBorrow=" + getPrireOfBorrow() +
            ", image='" + getImage() + "'" +
            ", dateCreate='" + getDateCreate() + "'" +
            ", dateUpdate='" + getDateUpdate() + "'" +
            ", userCreate='" + getUserCreate() + "'" +
            "}";
    }
}

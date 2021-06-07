package com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.web.rest.TestUtil;

public class BookTitleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookTitle.class);
        BookTitle bookTitle1 = new BookTitle();
        bookTitle1.setId(1L);
        BookTitle bookTitle2 = new BookTitle();
        bookTitle2.setId(bookTitle1.getId());
        assertThat(bookTitle1).isEqualTo(bookTitle2);
        bookTitle2.setId(2L);
        assertThat(bookTitle1).isNotEqualTo(bookTitle2);
        bookTitle1.setId(null);
        assertThat(bookTitle1).isNotEqualTo(bookTitle2);
    }
}

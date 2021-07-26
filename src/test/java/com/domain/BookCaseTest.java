package com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.web.rest.TestUtil;

public class BookCaseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookCase.class);
        BookCase bookCase1 = new BookCase();
        bookCase1.setId(1L);
        BookCase bookCase2 = new BookCase();
        bookCase2.setId(bookCase1.getId());
        assertThat(bookCase1).isEqualTo(bookCase2);
        bookCase2.setId(2L);
        assertThat(bookCase1).isNotEqualTo(bookCase2);
        bookCase1.setId(null);
        assertThat(bookCase1).isNotEqualTo(bookCase2);
    }
}

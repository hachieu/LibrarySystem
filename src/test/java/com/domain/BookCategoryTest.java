package com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.web.rest.TestUtil;

public class BookCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookCategory.class);
        BookCategory bookCategory1 = new BookCategory();
        bookCategory1.setId(1L);
        BookCategory bookCategory2 = new BookCategory();
        bookCategory2.setId(bookCategory1.getId());
        assertThat(bookCategory1).isEqualTo(bookCategory2);
        bookCategory2.setId(2L);
        assertThat(bookCategory1).isNotEqualTo(bookCategory2);
        bookCategory1.setId(null);
        assertThat(bookCategory1).isNotEqualTo(bookCategory2);
    }
}

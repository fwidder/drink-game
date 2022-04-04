package com.github.fwidder.drinkgame.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.fwidder.drinkgame.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AufgabeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aufgabe.class);
        Aufgabe aufgabe1 = new Aufgabe();
        aufgabe1.setId(1L);
        Aufgabe aufgabe2 = new Aufgabe();
        aufgabe2.setId(aufgabe1.getId());
        assertThat(aufgabe1).isEqualTo(aufgabe2);
        aufgabe2.setId(2L);
        assertThat(aufgabe1).isNotEqualTo(aufgabe2);
        aufgabe1.setId(null);
        assertThat(aufgabe1).isNotEqualTo(aufgabe2);
    }
}

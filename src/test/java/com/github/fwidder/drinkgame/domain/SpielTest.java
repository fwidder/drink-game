package com.github.fwidder.drinkgame.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.fwidder.drinkgame.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpielTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spiel.class);
        Spiel spiel1 = new Spiel();
        spiel1.setId(1L);
        Spiel spiel2 = new Spiel();
        spiel2.setId(spiel1.getId());
        assertThat(spiel1).isEqualTo(spiel2);
        spiel2.setId(2L);
        assertThat(spiel1).isNotEqualTo(spiel2);
        spiel1.setId(null);
        assertThat(spiel1).isNotEqualTo(spiel2);
    }
}

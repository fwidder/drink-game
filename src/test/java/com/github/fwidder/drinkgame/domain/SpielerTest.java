package com.github.fwidder.drinkgame.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.fwidder.drinkgame.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpielerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spieler.class);
        Spieler spieler1 = new Spieler();
        spieler1.setId(1L);
        Spieler spieler2 = new Spieler();
        spieler2.setId(spieler1.getId());
        assertThat(spieler1).isEqualTo(spieler2);
        spieler2.setId(2L);
        assertThat(spieler1).isNotEqualTo(spieler2);
        spieler1.setId(null);
        assertThat(spieler1).isNotEqualTo(spieler2);
    }
}

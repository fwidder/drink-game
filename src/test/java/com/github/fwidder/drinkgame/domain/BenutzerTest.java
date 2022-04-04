package com.github.fwidder.drinkgame.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.fwidder.drinkgame.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BenutzerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Benutzer.class);
        Benutzer benutzer1 = new Benutzer();
        benutzer1.setId(1L);
        Benutzer benutzer2 = new Benutzer();
        benutzer2.setId(benutzer1.getId());
        assertThat(benutzer1).isEqualTo(benutzer2);
        benutzer2.setId(2L);
        assertThat(benutzer1).isNotEqualTo(benutzer2);
        benutzer1.setId(null);
        assertThat(benutzer1).isNotEqualTo(benutzer2);
    }
}

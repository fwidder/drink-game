package com.github.fwidder.drinkgame.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.fwidder.drinkgame.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GetraenkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Getraenk.class);
        Getraenk getraenk1 = new Getraenk();
        getraenk1.setId(1L);
        Getraenk getraenk2 = new Getraenk();
        getraenk2.setId(getraenk1.getId());
        assertThat(getraenk1).isEqualTo(getraenk2);
        getraenk2.setId(2L);
        assertThat(getraenk1).isNotEqualTo(getraenk2);
        getraenk1.setId(null);
        assertThat(getraenk1).isNotEqualTo(getraenk2);
    }
}

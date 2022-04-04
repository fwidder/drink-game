package com.github.fwidder.drinkgame.repository;

import com.github.fwidder.drinkgame.domain.Spieler;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SpielerRepositoryWithBagRelationships {
    Optional<Spieler> fetchBagRelationships(Optional<Spieler> spieler);

    List<Spieler> fetchBagRelationships(List<Spieler> spielers);

    Page<Spieler> fetchBagRelationships(Page<Spieler> spielers);
}

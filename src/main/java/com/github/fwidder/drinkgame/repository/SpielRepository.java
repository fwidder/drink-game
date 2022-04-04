package com.github.fwidder.drinkgame.repository;

import com.github.fwidder.drinkgame.domain.Spiel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Spiel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpielRepository extends JpaRepository<Spiel, Long> {}

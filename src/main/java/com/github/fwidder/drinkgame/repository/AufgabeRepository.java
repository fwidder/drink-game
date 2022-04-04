package com.github.fwidder.drinkgame.repository;

import com.github.fwidder.drinkgame.domain.Aufgabe;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Aufgabe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AufgabeRepository extends JpaRepository<Aufgabe, Long> {}

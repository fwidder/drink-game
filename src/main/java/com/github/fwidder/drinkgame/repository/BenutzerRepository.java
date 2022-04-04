package com.github.fwidder.drinkgame.repository;

import com.github.fwidder.drinkgame.domain.Benutzer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Benutzer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BenutzerRepository extends JpaRepository<Benutzer, Long> {}

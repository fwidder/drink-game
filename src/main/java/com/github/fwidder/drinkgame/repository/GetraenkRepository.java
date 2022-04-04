package com.github.fwidder.drinkgame.repository;

import com.github.fwidder.drinkgame.domain.Getraenk;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Getraenk entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GetraenkRepository extends JpaRepository<Getraenk, Long> {}

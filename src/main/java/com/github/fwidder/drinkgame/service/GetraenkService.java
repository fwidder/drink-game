package com.github.fwidder.drinkgame.service;

import com.github.fwidder.drinkgame.domain.Getraenk;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Getraenk}.
 */
public interface GetraenkService {
    /**
     * Save a getraenk.
     *
     * @param getraenk the entity to save.
     * @return the persisted entity.
     */
    Getraenk save(Getraenk getraenk);

    /**
     * Updates a getraenk.
     *
     * @param getraenk the entity to update.
     * @return the persisted entity.
     */
    Getraenk update(Getraenk getraenk);

    /**
     * Partially updates a getraenk.
     *
     * @param getraenk the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Getraenk> partialUpdate(Getraenk getraenk);

    /**
     * Get all the getraenks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Getraenk> findAll(Pageable pageable);

    /**
     * Get the "id" getraenk.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Getraenk> findOne(Long id);

    /**
     * Delete the "id" getraenk.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

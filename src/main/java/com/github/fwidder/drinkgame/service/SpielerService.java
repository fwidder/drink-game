package com.github.fwidder.drinkgame.service;

import com.github.fwidder.drinkgame.domain.Spieler;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Spieler}.
 */
public interface SpielerService {
    /**
     * Save a spieler.
     *
     * @param spieler the entity to save.
     * @return the persisted entity.
     */
    Spieler save(Spieler spieler);

    /**
     * Updates a spieler.
     *
     * @param spieler the entity to update.
     * @return the persisted entity.
     */
    Spieler update(Spieler spieler);

    /**
     * Partially updates a spieler.
     *
     * @param spieler the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Spieler> partialUpdate(Spieler spieler);

    /**
     * Get all the spielers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Spieler> findAll(Pageable pageable);

    /**
     * Get all the spielers with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Spieler> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" spieler.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Spieler> findOne(Long id);

    /**
     * Delete the "id" spieler.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

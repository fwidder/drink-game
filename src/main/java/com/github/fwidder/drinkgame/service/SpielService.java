package com.github.fwidder.drinkgame.service;

import com.github.fwidder.drinkgame.domain.Spiel;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Spiel}.
 */
public interface SpielService {
    /**
     * Save a spiel.
     *
     * @param spiel the entity to save.
     * @return the persisted entity.
     */
    Spiel save(Spiel spiel);

    /**
     * Updates a spiel.
     *
     * @param spiel the entity to update.
     * @return the persisted entity.
     */
    Spiel update(Spiel spiel);

    /**
     * Partially updates a spiel.
     *
     * @param spiel the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Spiel> partialUpdate(Spiel spiel);

    /**
     * Get all the spiels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Spiel> findAll(Pageable pageable);

    /**
     * Get the "id" spiel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Spiel> findOne(Long id);

    /**
     * Delete the "id" spiel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

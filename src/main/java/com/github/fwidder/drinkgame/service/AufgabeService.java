package com.github.fwidder.drinkgame.service;

import com.github.fwidder.drinkgame.domain.Aufgabe;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Aufgabe}.
 */
public interface AufgabeService {
    /**
     * Save a aufgabe.
     *
     * @param aufgabe the entity to save.
     * @return the persisted entity.
     */
    Aufgabe save(Aufgabe aufgabe);

    /**
     * Updates a aufgabe.
     *
     * @param aufgabe the entity to update.
     * @return the persisted entity.
     */
    Aufgabe update(Aufgabe aufgabe);

    /**
     * Partially updates a aufgabe.
     *
     * @param aufgabe the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Aufgabe> partialUpdate(Aufgabe aufgabe);

    /**
     * Get all the aufgabes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Aufgabe> findAll(Pageable pageable);

    /**
     * Get the "id" aufgabe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Aufgabe> findOne(Long id);

    /**
     * Delete the "id" aufgabe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

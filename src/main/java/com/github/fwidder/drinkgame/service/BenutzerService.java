package com.github.fwidder.drinkgame.service;

import com.github.fwidder.drinkgame.domain.Benutzer;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Benutzer}.
 */
public interface BenutzerService {
    /**
     * Save a benutzer.
     *
     * @param benutzer the entity to save.
     * @return the persisted entity.
     */
    Benutzer save(Benutzer benutzer);

    /**
     * Updates a benutzer.
     *
     * @param benutzer the entity to update.
     * @return the persisted entity.
     */
    Benutzer update(Benutzer benutzer);

    /**
     * Partially updates a benutzer.
     *
     * @param benutzer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Benutzer> partialUpdate(Benutzer benutzer);

    /**
     * Get all the benutzers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Benutzer> findAll(Pageable pageable);

    /**
     * Get the "id" benutzer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Benutzer> findOne(Long id);

    /**
     * Delete the "id" benutzer.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

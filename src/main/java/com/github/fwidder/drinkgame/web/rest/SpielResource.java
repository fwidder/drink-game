package com.github.fwidder.drinkgame.web.rest;

import com.github.fwidder.drinkgame.domain.Spiel;
import com.github.fwidder.drinkgame.repository.SpielRepository;
import com.github.fwidder.drinkgame.service.SpielService;
import com.github.fwidder.drinkgame.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.github.fwidder.drinkgame.domain.Spiel}.
 */
@RestController
@RequestMapping("/api")
public class SpielResource {

    private final Logger log = LoggerFactory.getLogger(SpielResource.class);

    private static final String ENTITY_NAME = "spiel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpielService spielService;

    private final SpielRepository spielRepository;

    public SpielResource(SpielService spielService, SpielRepository spielRepository) {
        this.spielService = spielService;
        this.spielRepository = spielRepository;
    }

    /**
     * {@code POST  /spiels} : Create a new spiel.
     *
     * @param spiel the spiel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spiel, or with status {@code 400 (Bad Request)} if the spiel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spiels")
    public ResponseEntity<Spiel> createSpiel(@Valid @RequestBody Spiel spiel) throws URISyntaxException {
        log.debug("REST request to save Spiel : {}", spiel);
        if (spiel.getId() != null) {
            throw new BadRequestAlertException("A new spiel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Spiel result = spielService.save(spiel);
        return ResponseEntity
            .created(new URI("/api/spiels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spiels/:id} : Updates an existing spiel.
     *
     * @param id the id of the spiel to save.
     * @param spiel the spiel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spiel,
     * or with status {@code 400 (Bad Request)} if the spiel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spiel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spiels/{id}")
    public ResponseEntity<Spiel> updateSpiel(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Spiel spiel)
        throws URISyntaxException {
        log.debug("REST request to update Spiel : {}, {}", id, spiel);
        if (spiel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spiel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Spiel result = spielService.update(spiel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spiel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spiels/:id} : Partial updates given fields of an existing spiel, field will ignore if it is null
     *
     * @param id the id of the spiel to save.
     * @param spiel the spiel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spiel,
     * or with status {@code 400 (Bad Request)} if the spiel is not valid,
     * or with status {@code 404 (Not Found)} if the spiel is not found,
     * or with status {@code 500 (Internal Server Error)} if the spiel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spiels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Spiel> partialUpdateSpiel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Spiel spiel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Spiel partially : {}, {}", id, spiel);
        if (spiel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spiel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Spiel> result = spielService.partialUpdate(spiel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spiel.getId().toString())
        );
    }

    /**
     * {@code GET  /spiels} : get all the spiels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spiels in body.
     */
    @GetMapping("/spiels")
    public ResponseEntity<List<Spiel>> getAllSpiels(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Spiels");
        Page<Spiel> page = spielService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /spiels/:id} : get the "id" spiel.
     *
     * @param id the id of the spiel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spiel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spiels/{id}")
    public ResponseEntity<Spiel> getSpiel(@PathVariable Long id) {
        log.debug("REST request to get Spiel : {}", id);
        Optional<Spiel> spiel = spielService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spiel);
    }

    /**
     * {@code DELETE  /spiels/:id} : delete the "id" spiel.
     *
     * @param id the id of the spiel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spiels/{id}")
    public ResponseEntity<Void> deleteSpiel(@PathVariable Long id) {
        log.debug("REST request to delete Spiel : {}", id);
        spielService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

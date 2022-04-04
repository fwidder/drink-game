package com.github.fwidder.drinkgame.web.rest;

import com.github.fwidder.drinkgame.domain.Spieler;
import com.github.fwidder.drinkgame.repository.SpielerRepository;
import com.github.fwidder.drinkgame.service.SpielerService;
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
 * REST controller for managing {@link com.github.fwidder.drinkgame.domain.Spieler}.
 */
@RestController
@RequestMapping("/api")
public class SpielerResource {

    private final Logger log = LoggerFactory.getLogger(SpielerResource.class);

    private static final String ENTITY_NAME = "spieler";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpielerService spielerService;

    private final SpielerRepository spielerRepository;

    public SpielerResource(SpielerService spielerService, SpielerRepository spielerRepository) {
        this.spielerService = spielerService;
        this.spielerRepository = spielerRepository;
    }

    /**
     * {@code POST  /spielers} : Create a new spieler.
     *
     * @param spieler the spieler to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spieler, or with status {@code 400 (Bad Request)} if the spieler has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spielers")
    public ResponseEntity<Spieler> createSpieler(@Valid @RequestBody Spieler spieler) throws URISyntaxException {
        log.debug("REST request to save Spieler : {}", spieler);
        if (spieler.getId() != null) {
            throw new BadRequestAlertException("A new spieler cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Spieler result = spielerService.save(spieler);
        return ResponseEntity
            .created(new URI("/api/spielers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spielers/:id} : Updates an existing spieler.
     *
     * @param id the id of the spieler to save.
     * @param spieler the spieler to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spieler,
     * or with status {@code 400 (Bad Request)} if the spieler is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spieler couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spielers/{id}")
    public ResponseEntity<Spieler> updateSpieler(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Spieler spieler
    ) throws URISyntaxException {
        log.debug("REST request to update Spieler : {}, {}", id, spieler);
        if (spieler.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spieler.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spielerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Spieler result = spielerService.update(spieler);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spieler.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /spielers/:id} : Partial updates given fields of an existing spieler, field will ignore if it is null
     *
     * @param id the id of the spieler to save.
     * @param spieler the spieler to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spieler,
     * or with status {@code 400 (Bad Request)} if the spieler is not valid,
     * or with status {@code 404 (Not Found)} if the spieler is not found,
     * or with status {@code 500 (Internal Server Error)} if the spieler couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/spielers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Spieler> partialUpdateSpieler(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Spieler spieler
    ) throws URISyntaxException {
        log.debug("REST request to partial update Spieler partially : {}, {}", id, spieler);
        if (spieler.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, spieler.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!spielerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Spieler> result = spielerService.partialUpdate(spieler);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, spieler.getId().toString())
        );
    }

    /**
     * {@code GET  /spielers} : get all the spielers.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spielers in body.
     */
    @GetMapping("/spielers")
    public ResponseEntity<List<Spieler>> getAllSpielers(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Spielers");
        Page<Spieler> page;
        if (eagerload) {
            page = spielerService.findAllWithEagerRelationships(pageable);
        } else {
            page = spielerService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /spielers/:id} : get the "id" spieler.
     *
     * @param id the id of the spieler to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spieler, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spielers/{id}")
    public ResponseEntity<Spieler> getSpieler(@PathVariable Long id) {
        log.debug("REST request to get Spieler : {}", id);
        Optional<Spieler> spieler = spielerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spieler);
    }

    /**
     * {@code DELETE  /spielers/:id} : delete the "id" spieler.
     *
     * @param id the id of the spieler to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spielers/{id}")
    public ResponseEntity<Void> deleteSpieler(@PathVariable Long id) {
        log.debug("REST request to delete Spieler : {}", id);
        spielerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

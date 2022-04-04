package com.github.fwidder.drinkgame.web.rest;

import com.github.fwidder.drinkgame.domain.Aufgabe;
import com.github.fwidder.drinkgame.repository.AufgabeRepository;
import com.github.fwidder.drinkgame.service.AufgabeService;
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
 * REST controller for managing {@link com.github.fwidder.drinkgame.domain.Aufgabe}.
 */
@RestController
@RequestMapping("/api")
public class AufgabeResource {

    private final Logger log = LoggerFactory.getLogger(AufgabeResource.class);

    private static final String ENTITY_NAME = "aufgabe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AufgabeService aufgabeService;

    private final AufgabeRepository aufgabeRepository;

    public AufgabeResource(AufgabeService aufgabeService, AufgabeRepository aufgabeRepository) {
        this.aufgabeService = aufgabeService;
        this.aufgabeRepository = aufgabeRepository;
    }

    /**
     * {@code POST  /aufgabes} : Create a new aufgabe.
     *
     * @param aufgabe the aufgabe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aufgabe, or with status {@code 400 (Bad Request)} if the aufgabe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/aufgabes")
    public ResponseEntity<Aufgabe> createAufgabe(@Valid @RequestBody Aufgabe aufgabe) throws URISyntaxException {
        log.debug("REST request to save Aufgabe : {}", aufgabe);
        if (aufgabe.getId() != null) {
            throw new BadRequestAlertException("A new aufgabe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Aufgabe result = aufgabeService.save(aufgabe);
        return ResponseEntity
            .created(new URI("/api/aufgabes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /aufgabes/:id} : Updates an existing aufgabe.
     *
     * @param id the id of the aufgabe to save.
     * @param aufgabe the aufgabe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aufgabe,
     * or with status {@code 400 (Bad Request)} if the aufgabe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aufgabe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/aufgabes/{id}")
    public ResponseEntity<Aufgabe> updateAufgabe(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Aufgabe aufgabe
    ) throws URISyntaxException {
        log.debug("REST request to update Aufgabe : {}, {}", id, aufgabe);
        if (aufgabe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aufgabe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aufgabeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Aufgabe result = aufgabeService.update(aufgabe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aufgabe.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /aufgabes/:id} : Partial updates given fields of an existing aufgabe, field will ignore if it is null
     *
     * @param id the id of the aufgabe to save.
     * @param aufgabe the aufgabe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aufgabe,
     * or with status {@code 400 (Bad Request)} if the aufgabe is not valid,
     * or with status {@code 404 (Not Found)} if the aufgabe is not found,
     * or with status {@code 500 (Internal Server Error)} if the aufgabe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/aufgabes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Aufgabe> partialUpdateAufgabe(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Aufgabe aufgabe
    ) throws URISyntaxException {
        log.debug("REST request to partial update Aufgabe partially : {}, {}", id, aufgabe);
        if (aufgabe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aufgabe.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aufgabeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Aufgabe> result = aufgabeService.partialUpdate(aufgabe);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, aufgabe.getId().toString())
        );
    }

    /**
     * {@code GET  /aufgabes} : get all the aufgabes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aufgabes in body.
     */
    @GetMapping("/aufgabes")
    public ResponseEntity<List<Aufgabe>> getAllAufgabes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Aufgabes");
        Page<Aufgabe> page = aufgabeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /aufgabes/:id} : get the "id" aufgabe.
     *
     * @param id the id of the aufgabe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aufgabe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/aufgabes/{id}")
    public ResponseEntity<Aufgabe> getAufgabe(@PathVariable Long id) {
        log.debug("REST request to get Aufgabe : {}", id);
        Optional<Aufgabe> aufgabe = aufgabeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aufgabe);
    }

    /**
     * {@code DELETE  /aufgabes/:id} : delete the "id" aufgabe.
     *
     * @param id the id of the aufgabe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/aufgabes/{id}")
    public ResponseEntity<Void> deleteAufgabe(@PathVariable Long id) {
        log.debug("REST request to delete Aufgabe : {}", id);
        aufgabeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

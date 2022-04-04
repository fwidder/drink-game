package com.github.fwidder.drinkgame.web.rest;

import com.github.fwidder.drinkgame.domain.Benutzer;
import com.github.fwidder.drinkgame.repository.BenutzerRepository;
import com.github.fwidder.drinkgame.service.BenutzerService;
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
 * REST controller for managing {@link com.github.fwidder.drinkgame.domain.Benutzer}.
 */
@RestController
@RequestMapping("/api")
public class BenutzerResource {

    private final Logger log = LoggerFactory.getLogger(BenutzerResource.class);

    private static final String ENTITY_NAME = "benutzer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BenutzerService benutzerService;

    private final BenutzerRepository benutzerRepository;

    public BenutzerResource(BenutzerService benutzerService, BenutzerRepository benutzerRepository) {
        this.benutzerService = benutzerService;
        this.benutzerRepository = benutzerRepository;
    }

    /**
     * {@code POST  /benutzers} : Create a new benutzer.
     *
     * @param benutzer the benutzer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new benutzer, or with status {@code 400 (Bad Request)} if the benutzer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/benutzers")
    public ResponseEntity<Benutzer> createBenutzer(@Valid @RequestBody Benutzer benutzer) throws URISyntaxException {
        log.debug("REST request to save Benutzer : {}", benutzer);
        if (benutzer.getId() != null) {
            throw new BadRequestAlertException("A new benutzer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Benutzer result = benutzerService.save(benutzer);
        return ResponseEntity
            .created(new URI("/api/benutzers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /benutzers/:id} : Updates an existing benutzer.
     *
     * @param id the id of the benutzer to save.
     * @param benutzer the benutzer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated benutzer,
     * or with status {@code 400 (Bad Request)} if the benutzer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the benutzer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/benutzers/{id}")
    public ResponseEntity<Benutzer> updateBenutzer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Benutzer benutzer
    ) throws URISyntaxException {
        log.debug("REST request to update Benutzer : {}, {}", id, benutzer);
        if (benutzer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, benutzer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!benutzerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Benutzer result = benutzerService.update(benutzer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, benutzer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /benutzers/:id} : Partial updates given fields of an existing benutzer, field will ignore if it is null
     *
     * @param id the id of the benutzer to save.
     * @param benutzer the benutzer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated benutzer,
     * or with status {@code 400 (Bad Request)} if the benutzer is not valid,
     * or with status {@code 404 (Not Found)} if the benutzer is not found,
     * or with status {@code 500 (Internal Server Error)} if the benutzer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/benutzers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Benutzer> partialUpdateBenutzer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Benutzer benutzer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Benutzer partially : {}, {}", id, benutzer);
        if (benutzer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, benutzer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!benutzerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Benutzer> result = benutzerService.partialUpdate(benutzer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, benutzer.getId().toString())
        );
    }

    /**
     * {@code GET  /benutzers} : get all the benutzers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of benutzers in body.
     */
    @GetMapping("/benutzers")
    public ResponseEntity<List<Benutzer>> getAllBenutzers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Benutzers");
        Page<Benutzer> page = benutzerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /benutzers/:id} : get the "id" benutzer.
     *
     * @param id the id of the benutzer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the benutzer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/benutzers/{id}")
    public ResponseEntity<Benutzer> getBenutzer(@PathVariable Long id) {
        log.debug("REST request to get Benutzer : {}", id);
        Optional<Benutzer> benutzer = benutzerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(benutzer);
    }

    /**
     * {@code DELETE  /benutzers/:id} : delete the "id" benutzer.
     *
     * @param id the id of the benutzer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/benutzers/{id}")
    public ResponseEntity<Void> deleteBenutzer(@PathVariable Long id) {
        log.debug("REST request to delete Benutzer : {}", id);
        benutzerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

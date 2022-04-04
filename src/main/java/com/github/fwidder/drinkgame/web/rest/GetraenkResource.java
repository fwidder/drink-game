package com.github.fwidder.drinkgame.web.rest;

import com.github.fwidder.drinkgame.domain.Getraenk;
import com.github.fwidder.drinkgame.repository.GetraenkRepository;
import com.github.fwidder.drinkgame.service.GetraenkService;
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
 * REST controller for managing {@link com.github.fwidder.drinkgame.domain.Getraenk}.
 */
@RestController
@RequestMapping("/api")
public class GetraenkResource {

    private final Logger log = LoggerFactory.getLogger(GetraenkResource.class);

    private static final String ENTITY_NAME = "getraenk";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GetraenkService getraenkService;

    private final GetraenkRepository getraenkRepository;

    public GetraenkResource(GetraenkService getraenkService, GetraenkRepository getraenkRepository) {
        this.getraenkService = getraenkService;
        this.getraenkRepository = getraenkRepository;
    }

    /**
     * {@code POST  /getraenks} : Create a new getraenk.
     *
     * @param getraenk the getraenk to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new getraenk, or with status {@code 400 (Bad Request)} if the getraenk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/getraenks")
    public ResponseEntity<Getraenk> createGetraenk(@Valid @RequestBody Getraenk getraenk) throws URISyntaxException {
        log.debug("REST request to save Getraenk : {}", getraenk);
        if (getraenk.getId() != null) {
            throw new BadRequestAlertException("A new getraenk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Getraenk result = getraenkService.save(getraenk);
        return ResponseEntity
            .created(new URI("/api/getraenks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /getraenks/:id} : Updates an existing getraenk.
     *
     * @param id the id of the getraenk to save.
     * @param getraenk the getraenk to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated getraenk,
     * or with status {@code 400 (Bad Request)} if the getraenk is not valid,
     * or with status {@code 500 (Internal Server Error)} if the getraenk couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/getraenks/{id}")
    public ResponseEntity<Getraenk> updateGetraenk(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Getraenk getraenk
    ) throws URISyntaxException {
        log.debug("REST request to update Getraenk : {}, {}", id, getraenk);
        if (getraenk.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, getraenk.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!getraenkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Getraenk result = getraenkService.update(getraenk);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, getraenk.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /getraenks/:id} : Partial updates given fields of an existing getraenk, field will ignore if it is null
     *
     * @param id the id of the getraenk to save.
     * @param getraenk the getraenk to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated getraenk,
     * or with status {@code 400 (Bad Request)} if the getraenk is not valid,
     * or with status {@code 404 (Not Found)} if the getraenk is not found,
     * or with status {@code 500 (Internal Server Error)} if the getraenk couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/getraenks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Getraenk> partialUpdateGetraenk(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Getraenk getraenk
    ) throws URISyntaxException {
        log.debug("REST request to partial update Getraenk partially : {}, {}", id, getraenk);
        if (getraenk.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, getraenk.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!getraenkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Getraenk> result = getraenkService.partialUpdate(getraenk);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, getraenk.getId().toString())
        );
    }

    /**
     * {@code GET  /getraenks} : get all the getraenks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of getraenks in body.
     */
    @GetMapping("/getraenks")
    public ResponseEntity<List<Getraenk>> getAllGetraenks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Getraenks");
        Page<Getraenk> page = getraenkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /getraenks/:id} : get the "id" getraenk.
     *
     * @param id the id of the getraenk to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the getraenk, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/getraenks/{id}")
    public ResponseEntity<Getraenk> getGetraenk(@PathVariable Long id) {
        log.debug("REST request to get Getraenk : {}", id);
        Optional<Getraenk> getraenk = getraenkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(getraenk);
    }

    /**
     * {@code DELETE  /getraenks/:id} : delete the "id" getraenk.
     *
     * @param id the id of the getraenk to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/getraenks/{id}")
    public ResponseEntity<Void> deleteGetraenk(@PathVariable Long id) {
        log.debug("REST request to delete Getraenk : {}", id);
        getraenkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.github.fwidder.drinkgame.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.fwidder.drinkgame.IntegrationTest;
import com.github.fwidder.drinkgame.domain.Getraenk;
import com.github.fwidder.drinkgame.domain.enumeration.Groesse;
import com.github.fwidder.drinkgame.repository.GetraenkRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GetraenkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GetraenkResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Groesse DEFAULT_GROESSE = Groesse.SHOT;
    private static final Groesse UPDATED_GROESSE = Groesse.GLAS;

    private static final String ENTITY_API_URL = "/api/getraenks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GetraenkRepository getraenkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGetraenkMockMvc;

    private Getraenk getraenk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Getraenk createEntity(EntityManager em) {
        Getraenk getraenk = new Getraenk().name(DEFAULT_NAME).groesse(DEFAULT_GROESSE);
        return getraenk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Getraenk createUpdatedEntity(EntityManager em) {
        Getraenk getraenk = new Getraenk().name(UPDATED_NAME).groesse(UPDATED_GROESSE);
        return getraenk;
    }

    @BeforeEach
    public void initTest() {
        getraenk = createEntity(em);
    }

    @Test
    @Transactional
    void createGetraenk() throws Exception {
        int databaseSizeBeforeCreate = getraenkRepository.findAll().size();
        // Create the Getraenk
        restGetraenkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(getraenk)))
            .andExpect(status().isCreated());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeCreate + 1);
        Getraenk testGetraenk = getraenkList.get(getraenkList.size() - 1);
        assertThat(testGetraenk.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGetraenk.getGroesse()).isEqualTo(DEFAULT_GROESSE);
    }

    @Test
    @Transactional
    void createGetraenkWithExistingId() throws Exception {
        // Create the Getraenk with an existing ID
        getraenk.setId(1L);

        int databaseSizeBeforeCreate = getraenkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGetraenkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(getraenk)))
            .andExpect(status().isBadRequest());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = getraenkRepository.findAll().size();
        // set the field null
        getraenk.setName(null);

        // Create the Getraenk, which fails.

        restGetraenkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(getraenk)))
            .andExpect(status().isBadRequest());

        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGroesseIsRequired() throws Exception {
        int databaseSizeBeforeTest = getraenkRepository.findAll().size();
        // set the field null
        getraenk.setGroesse(null);

        // Create the Getraenk, which fails.

        restGetraenkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(getraenk)))
            .andExpect(status().isBadRequest());

        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGetraenks() throws Exception {
        // Initialize the database
        getraenkRepository.saveAndFlush(getraenk);

        // Get all the getraenkList
        restGetraenkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(getraenk.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].groesse").value(hasItem(DEFAULT_GROESSE.toString())));
    }

    @Test
    @Transactional
    void getGetraenk() throws Exception {
        // Initialize the database
        getraenkRepository.saveAndFlush(getraenk);

        // Get the getraenk
        restGetraenkMockMvc
            .perform(get(ENTITY_API_URL_ID, getraenk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(getraenk.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.groesse").value(DEFAULT_GROESSE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingGetraenk() throws Exception {
        // Get the getraenk
        restGetraenkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGetraenk() throws Exception {
        // Initialize the database
        getraenkRepository.saveAndFlush(getraenk);

        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();

        // Update the getraenk
        Getraenk updatedGetraenk = getraenkRepository.findById(getraenk.getId()).get();
        // Disconnect from session so that the updates on updatedGetraenk are not directly saved in db
        em.detach(updatedGetraenk);
        updatedGetraenk.name(UPDATED_NAME).groesse(UPDATED_GROESSE);

        restGetraenkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGetraenk.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGetraenk))
            )
            .andExpect(status().isOk());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
        Getraenk testGetraenk = getraenkList.get(getraenkList.size() - 1);
        assertThat(testGetraenk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGetraenk.getGroesse()).isEqualTo(UPDATED_GROESSE);
    }

    @Test
    @Transactional
    void putNonExistingGetraenk() throws Exception {
        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();
        getraenk.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGetraenkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, getraenk.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(getraenk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGetraenk() throws Exception {
        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();
        getraenk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGetraenkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(getraenk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGetraenk() throws Exception {
        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();
        getraenk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGetraenkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(getraenk)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGetraenkWithPatch() throws Exception {
        // Initialize the database
        getraenkRepository.saveAndFlush(getraenk);

        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();

        // Update the getraenk using partial update
        Getraenk partialUpdatedGetraenk = new Getraenk();
        partialUpdatedGetraenk.setId(getraenk.getId());

        partialUpdatedGetraenk.name(UPDATED_NAME).groesse(UPDATED_GROESSE);

        restGetraenkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGetraenk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGetraenk))
            )
            .andExpect(status().isOk());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
        Getraenk testGetraenk = getraenkList.get(getraenkList.size() - 1);
        assertThat(testGetraenk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGetraenk.getGroesse()).isEqualTo(UPDATED_GROESSE);
    }

    @Test
    @Transactional
    void fullUpdateGetraenkWithPatch() throws Exception {
        // Initialize the database
        getraenkRepository.saveAndFlush(getraenk);

        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();

        // Update the getraenk using partial update
        Getraenk partialUpdatedGetraenk = new Getraenk();
        partialUpdatedGetraenk.setId(getraenk.getId());

        partialUpdatedGetraenk.name(UPDATED_NAME).groesse(UPDATED_GROESSE);

        restGetraenkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGetraenk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGetraenk))
            )
            .andExpect(status().isOk());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
        Getraenk testGetraenk = getraenkList.get(getraenkList.size() - 1);
        assertThat(testGetraenk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGetraenk.getGroesse()).isEqualTo(UPDATED_GROESSE);
    }

    @Test
    @Transactional
    void patchNonExistingGetraenk() throws Exception {
        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();
        getraenk.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGetraenkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, getraenk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(getraenk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGetraenk() throws Exception {
        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();
        getraenk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGetraenkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(getraenk))
            )
            .andExpect(status().isBadRequest());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGetraenk() throws Exception {
        int databaseSizeBeforeUpdate = getraenkRepository.findAll().size();
        getraenk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGetraenkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(getraenk)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Getraenk in the database
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGetraenk() throws Exception {
        // Initialize the database
        getraenkRepository.saveAndFlush(getraenk);

        int databaseSizeBeforeDelete = getraenkRepository.findAll().size();

        // Delete the getraenk
        restGetraenkMockMvc
            .perform(delete(ENTITY_API_URL_ID, getraenk.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Getraenk> getraenkList = getraenkRepository.findAll();
        assertThat(getraenkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

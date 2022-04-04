package com.github.fwidder.drinkgame.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.fwidder.drinkgame.IntegrationTest;
import com.github.fwidder.drinkgame.domain.Benutzer;
import com.github.fwidder.drinkgame.domain.Spiel;
import com.github.fwidder.drinkgame.repository.SpielRepository;
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
 * Integration tests for the {@link SpielResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpielResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/spiels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpielRepository spielRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpielMockMvc;

    private Spiel spiel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spiel createEntity(EntityManager em) {
        Spiel spiel = new Spiel().name(DEFAULT_NAME);
        // Add required entity
        Benutzer benutzer;
        if (TestUtil.findAll(em, Benutzer.class).isEmpty()) {
            benutzer = BenutzerResourceIT.createEntity(em);
            em.persist(benutzer);
            em.flush();
        } else {
            benutzer = TestUtil.findAll(em, Benutzer.class).get(0);
        }
        spiel.setBenutzer(benutzer);
        return spiel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spiel createUpdatedEntity(EntityManager em) {
        Spiel spiel = new Spiel().name(UPDATED_NAME);
        // Add required entity
        Benutzer benutzer;
        if (TestUtil.findAll(em, Benutzer.class).isEmpty()) {
            benutzer = BenutzerResourceIT.createUpdatedEntity(em);
            em.persist(benutzer);
            em.flush();
        } else {
            benutzer = TestUtil.findAll(em, Benutzer.class).get(0);
        }
        spiel.setBenutzer(benutzer);
        return spiel;
    }

    @BeforeEach
    public void initTest() {
        spiel = createEntity(em);
    }

    @Test
    @Transactional
    void createSpiel() throws Exception {
        int databaseSizeBeforeCreate = spielRepository.findAll().size();
        // Create the Spiel
        restSpielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spiel)))
            .andExpect(status().isCreated());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeCreate + 1);
        Spiel testSpiel = spielList.get(spielList.size() - 1);
        assertThat(testSpiel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSpielWithExistingId() throws Exception {
        // Create the Spiel with an existing ID
        spiel.setId(1L);

        int databaseSizeBeforeCreate = spielRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spiel)))
            .andExpect(status().isBadRequest());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = spielRepository.findAll().size();
        // set the field null
        spiel.setName(null);

        // Create the Spiel, which fails.

        restSpielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spiel)))
            .andExpect(status().isBadRequest());

        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpiels() throws Exception {
        // Initialize the database
        spielRepository.saveAndFlush(spiel);

        // Get all the spielList
        restSpielMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spiel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSpiel() throws Exception {
        // Initialize the database
        spielRepository.saveAndFlush(spiel);

        // Get the spiel
        restSpielMockMvc
            .perform(get(ENTITY_API_URL_ID, spiel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spiel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSpiel() throws Exception {
        // Get the spiel
        restSpielMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpiel() throws Exception {
        // Initialize the database
        spielRepository.saveAndFlush(spiel);

        int databaseSizeBeforeUpdate = spielRepository.findAll().size();

        // Update the spiel
        Spiel updatedSpiel = spielRepository.findById(spiel.getId()).get();
        // Disconnect from session so that the updates on updatedSpiel are not directly saved in db
        em.detach(updatedSpiel);
        updatedSpiel.name(UPDATED_NAME);

        restSpielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpiel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpiel))
            )
            .andExpect(status().isOk());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
        Spiel testSpiel = spielList.get(spielList.size() - 1);
        assertThat(testSpiel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSpiel() throws Exception {
        int databaseSizeBeforeUpdate = spielRepository.findAll().size();
        spiel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spiel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spiel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpiel() throws Exception {
        int databaseSizeBeforeUpdate = spielRepository.findAll().size();
        spiel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spiel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpiel() throws Exception {
        int databaseSizeBeforeUpdate = spielRepository.findAll().size();
        spiel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpielMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spiel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpielWithPatch() throws Exception {
        // Initialize the database
        spielRepository.saveAndFlush(spiel);

        int databaseSizeBeforeUpdate = spielRepository.findAll().size();

        // Update the spiel using partial update
        Spiel partialUpdatedSpiel = new Spiel();
        partialUpdatedSpiel.setId(spiel.getId());

        partialUpdatedSpiel.name(UPDATED_NAME);

        restSpielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpiel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpiel))
            )
            .andExpect(status().isOk());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
        Spiel testSpiel = spielList.get(spielList.size() - 1);
        assertThat(testSpiel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSpielWithPatch() throws Exception {
        // Initialize the database
        spielRepository.saveAndFlush(spiel);

        int databaseSizeBeforeUpdate = spielRepository.findAll().size();

        // Update the spiel using partial update
        Spiel partialUpdatedSpiel = new Spiel();
        partialUpdatedSpiel.setId(spiel.getId());

        partialUpdatedSpiel.name(UPDATED_NAME);

        restSpielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpiel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpiel))
            )
            .andExpect(status().isOk());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
        Spiel testSpiel = spielList.get(spielList.size() - 1);
        assertThat(testSpiel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSpiel() throws Exception {
        int databaseSizeBeforeUpdate = spielRepository.findAll().size();
        spiel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spiel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spiel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpiel() throws Exception {
        int databaseSizeBeforeUpdate = spielRepository.findAll().size();
        spiel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spiel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpiel() throws Exception {
        int databaseSizeBeforeUpdate = spielRepository.findAll().size();
        spiel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpielMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(spiel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spiel in the database
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpiel() throws Exception {
        // Initialize the database
        spielRepository.saveAndFlush(spiel);

        int databaseSizeBeforeDelete = spielRepository.findAll().size();

        // Delete the spiel
        restSpielMockMvc
            .perform(delete(ENTITY_API_URL_ID, spiel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spiel> spielList = spielRepository.findAll();
        assertThat(spielList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

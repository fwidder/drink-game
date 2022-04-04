package com.github.fwidder.drinkgame.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.fwidder.drinkgame.IntegrationTest;
import com.github.fwidder.drinkgame.domain.Benutzer;
import com.github.fwidder.drinkgame.domain.User;
import com.github.fwidder.drinkgame.repository.BenutzerRepository;
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
 * Integration tests for the {@link BenutzerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BenutzerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/benutzers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BenutzerRepository benutzerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBenutzerMockMvc;

    private Benutzer benutzer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Benutzer createEntity(EntityManager em) {
        Benutzer benutzer = new Benutzer().name(DEFAULT_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        benutzer.setUser(user);
        return benutzer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Benutzer createUpdatedEntity(EntityManager em) {
        Benutzer benutzer = new Benutzer().name(UPDATED_NAME);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        benutzer.setUser(user);
        return benutzer;
    }

    @BeforeEach
    public void initTest() {
        benutzer = createEntity(em);
    }

    @Test
    @Transactional
    void createBenutzer() throws Exception {
        int databaseSizeBeforeCreate = benutzerRepository.findAll().size();
        // Create the Benutzer
        restBenutzerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(benutzer)))
            .andExpect(status().isCreated());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeCreate + 1);
        Benutzer testBenutzer = benutzerList.get(benutzerList.size() - 1);
        assertThat(testBenutzer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createBenutzerWithExistingId() throws Exception {
        // Create the Benutzer with an existing ID
        benutzer.setId(1L);

        int databaseSizeBeforeCreate = benutzerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBenutzerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(benutzer)))
            .andExpect(status().isBadRequest());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = benutzerRepository.findAll().size();
        // set the field null
        benutzer.setName(null);

        // Create the Benutzer, which fails.

        restBenutzerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(benutzer)))
            .andExpect(status().isBadRequest());

        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBenutzers() throws Exception {
        // Initialize the database
        benutzerRepository.saveAndFlush(benutzer);

        // Get all the benutzerList
        restBenutzerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(benutzer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getBenutzer() throws Exception {
        // Initialize the database
        benutzerRepository.saveAndFlush(benutzer);

        // Get the benutzer
        restBenutzerMockMvc
            .perform(get(ENTITY_API_URL_ID, benutzer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(benutzer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingBenutzer() throws Exception {
        // Get the benutzer
        restBenutzerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBenutzer() throws Exception {
        // Initialize the database
        benutzerRepository.saveAndFlush(benutzer);

        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();

        // Update the benutzer
        Benutzer updatedBenutzer = benutzerRepository.findById(benutzer.getId()).get();
        // Disconnect from session so that the updates on updatedBenutzer are not directly saved in db
        em.detach(updatedBenutzer);
        updatedBenutzer.name(UPDATED_NAME);

        restBenutzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBenutzer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBenutzer))
            )
            .andExpect(status().isOk());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
        Benutzer testBenutzer = benutzerList.get(benutzerList.size() - 1);
        assertThat(testBenutzer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBenutzer() throws Exception {
        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();
        benutzer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBenutzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, benutzer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(benutzer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBenutzer() throws Exception {
        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();
        benutzer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBenutzerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(benutzer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBenutzer() throws Exception {
        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();
        benutzer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBenutzerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(benutzer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBenutzerWithPatch() throws Exception {
        // Initialize the database
        benutzerRepository.saveAndFlush(benutzer);

        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();

        // Update the benutzer using partial update
        Benutzer partialUpdatedBenutzer = new Benutzer();
        partialUpdatedBenutzer.setId(benutzer.getId());

        partialUpdatedBenutzer.name(UPDATED_NAME);

        restBenutzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBenutzer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBenutzer))
            )
            .andExpect(status().isOk());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
        Benutzer testBenutzer = benutzerList.get(benutzerList.size() - 1);
        assertThat(testBenutzer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBenutzerWithPatch() throws Exception {
        // Initialize the database
        benutzerRepository.saveAndFlush(benutzer);

        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();

        // Update the benutzer using partial update
        Benutzer partialUpdatedBenutzer = new Benutzer();
        partialUpdatedBenutzer.setId(benutzer.getId());

        partialUpdatedBenutzer.name(UPDATED_NAME);

        restBenutzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBenutzer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBenutzer))
            )
            .andExpect(status().isOk());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
        Benutzer testBenutzer = benutzerList.get(benutzerList.size() - 1);
        assertThat(testBenutzer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBenutzer() throws Exception {
        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();
        benutzer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBenutzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, benutzer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(benutzer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBenutzer() throws Exception {
        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();
        benutzer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBenutzerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(benutzer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBenutzer() throws Exception {
        int databaseSizeBeforeUpdate = benutzerRepository.findAll().size();
        benutzer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBenutzerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(benutzer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Benutzer in the database
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBenutzer() throws Exception {
        // Initialize the database
        benutzerRepository.saveAndFlush(benutzer);

        int databaseSizeBeforeDelete = benutzerRepository.findAll().size();

        // Delete the benutzer
        restBenutzerMockMvc
            .perform(delete(ENTITY_API_URL_ID, benutzer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Benutzer> benutzerList = benutzerRepository.findAll();
        assertThat(benutzerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.github.fwidder.drinkgame.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.fwidder.drinkgame.IntegrationTest;
import com.github.fwidder.drinkgame.domain.Aufgabe;
import com.github.fwidder.drinkgame.domain.enumeration.Kategorie;
import com.github.fwidder.drinkgame.domain.enumeration.Level;
import com.github.fwidder.drinkgame.repository.AufgabeRepository;
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
 * Integration tests for the {@link AufgabeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AufgabeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KURZTEXT = "AAAAAAAAAA";
    private static final String UPDATED_KURZTEXT = "BBBBBBBBBB";

    private static final String DEFAULT_BESCHREIBUNG = "AAAAAAAAAA";
    private static final String UPDATED_BESCHREIBUNG = "BBBBBBBBBB";

    private static final Kategorie DEFAULT_KATEGORIE = Kategorie.XXX;
    private static final Kategorie UPDATED_KATEGORIE = Kategorie.FRAGEN;

    private static final Level DEFAULT_LEVEL = Level.EASY;
    private static final Level UPDATED_LEVEL = Level.MEDIUM;

    private static final String ENTITY_API_URL = "/api/aufgabes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AufgabeRepository aufgabeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAufgabeMockMvc;

    private Aufgabe aufgabe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aufgabe createEntity(EntityManager em) {
        Aufgabe aufgabe = new Aufgabe()
            .name(DEFAULT_NAME)
            .kurztext(DEFAULT_KURZTEXT)
            .beschreibung(DEFAULT_BESCHREIBUNG)
            .kategorie(DEFAULT_KATEGORIE)
            .level(DEFAULT_LEVEL);
        return aufgabe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Aufgabe createUpdatedEntity(EntityManager em) {
        Aufgabe aufgabe = new Aufgabe()
            .name(UPDATED_NAME)
            .kurztext(UPDATED_KURZTEXT)
            .beschreibung(UPDATED_BESCHREIBUNG)
            .kategorie(UPDATED_KATEGORIE)
            .level(UPDATED_LEVEL);
        return aufgabe;
    }

    @BeforeEach
    public void initTest() {
        aufgabe = createEntity(em);
    }

    @Test
    @Transactional
    void createAufgabe() throws Exception {
        int databaseSizeBeforeCreate = aufgabeRepository.findAll().size();
        // Create the Aufgabe
        restAufgabeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isCreated());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeCreate + 1);
        Aufgabe testAufgabe = aufgabeList.get(aufgabeList.size() - 1);
        assertThat(testAufgabe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAufgabe.getKurztext()).isEqualTo(DEFAULT_KURZTEXT);
        assertThat(testAufgabe.getBeschreibung()).isEqualTo(DEFAULT_BESCHREIBUNG);
        assertThat(testAufgabe.getKategorie()).isEqualTo(DEFAULT_KATEGORIE);
        assertThat(testAufgabe.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void createAufgabeWithExistingId() throws Exception {
        // Create the Aufgabe with an existing ID
        aufgabe.setId(1L);

        int databaseSizeBeforeCreate = aufgabeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAufgabeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isBadRequest());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = aufgabeRepository.findAll().size();
        // set the field null
        aufgabe.setName(null);

        // Create the Aufgabe, which fails.

        restAufgabeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isBadRequest());

        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKurztextIsRequired() throws Exception {
        int databaseSizeBeforeTest = aufgabeRepository.findAll().size();
        // set the field null
        aufgabe.setKurztext(null);

        // Create the Aufgabe, which fails.

        restAufgabeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isBadRequest());

        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBeschreibungIsRequired() throws Exception {
        int databaseSizeBeforeTest = aufgabeRepository.findAll().size();
        // set the field null
        aufgabe.setBeschreibung(null);

        // Create the Aufgabe, which fails.

        restAufgabeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isBadRequest());

        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkKategorieIsRequired() throws Exception {
        int databaseSizeBeforeTest = aufgabeRepository.findAll().size();
        // set the field null
        aufgabe.setKategorie(null);

        // Create the Aufgabe, which fails.

        restAufgabeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isBadRequest());

        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = aufgabeRepository.findAll().size();
        // set the field null
        aufgabe.setLevel(null);

        // Create the Aufgabe, which fails.

        restAufgabeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isBadRequest());

        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAufgabes() throws Exception {
        // Initialize the database
        aufgabeRepository.saveAndFlush(aufgabe);

        // Get all the aufgabeList
        restAufgabeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aufgabe.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].kurztext").value(hasItem(DEFAULT_KURZTEXT)))
            .andExpect(jsonPath("$.[*].beschreibung").value(hasItem(DEFAULT_BESCHREIBUNG)))
            .andExpect(jsonPath("$.[*].kategorie").value(hasItem(DEFAULT_KATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL.toString())));
    }

    @Test
    @Transactional
    void getAufgabe() throws Exception {
        // Initialize the database
        aufgabeRepository.saveAndFlush(aufgabe);

        // Get the aufgabe
        restAufgabeMockMvc
            .perform(get(ENTITY_API_URL_ID, aufgabe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aufgabe.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.kurztext").value(DEFAULT_KURZTEXT))
            .andExpect(jsonPath("$.beschreibung").value(DEFAULT_BESCHREIBUNG))
            .andExpect(jsonPath("$.kategorie").value(DEFAULT_KATEGORIE.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAufgabe() throws Exception {
        // Get the aufgabe
        restAufgabeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAufgabe() throws Exception {
        // Initialize the database
        aufgabeRepository.saveAndFlush(aufgabe);

        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();

        // Update the aufgabe
        Aufgabe updatedAufgabe = aufgabeRepository.findById(aufgabe.getId()).get();
        // Disconnect from session so that the updates on updatedAufgabe are not directly saved in db
        em.detach(updatedAufgabe);
        updatedAufgabe
            .name(UPDATED_NAME)
            .kurztext(UPDATED_KURZTEXT)
            .beschreibung(UPDATED_BESCHREIBUNG)
            .kategorie(UPDATED_KATEGORIE)
            .level(UPDATED_LEVEL);

        restAufgabeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAufgabe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAufgabe))
            )
            .andExpect(status().isOk());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
        Aufgabe testAufgabe = aufgabeList.get(aufgabeList.size() - 1);
        assertThat(testAufgabe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAufgabe.getKurztext()).isEqualTo(UPDATED_KURZTEXT);
        assertThat(testAufgabe.getBeschreibung()).isEqualTo(UPDATED_BESCHREIBUNG);
        assertThat(testAufgabe.getKategorie()).isEqualTo(UPDATED_KATEGORIE);
        assertThat(testAufgabe.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void putNonExistingAufgabe() throws Exception {
        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();
        aufgabe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAufgabeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aufgabe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aufgabe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAufgabe() throws Exception {
        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();
        aufgabe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAufgabeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aufgabe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAufgabe() throws Exception {
        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();
        aufgabe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAufgabeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAufgabeWithPatch() throws Exception {
        // Initialize the database
        aufgabeRepository.saveAndFlush(aufgabe);

        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();

        // Update the aufgabe using partial update
        Aufgabe partialUpdatedAufgabe = new Aufgabe();
        partialUpdatedAufgabe.setId(aufgabe.getId());

        partialUpdatedAufgabe.name(UPDATED_NAME).kurztext(UPDATED_KURZTEXT).beschreibung(UPDATED_BESCHREIBUNG).kategorie(UPDATED_KATEGORIE);

        restAufgabeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAufgabe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAufgabe))
            )
            .andExpect(status().isOk());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
        Aufgabe testAufgabe = aufgabeList.get(aufgabeList.size() - 1);
        assertThat(testAufgabe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAufgabe.getKurztext()).isEqualTo(UPDATED_KURZTEXT);
        assertThat(testAufgabe.getBeschreibung()).isEqualTo(UPDATED_BESCHREIBUNG);
        assertThat(testAufgabe.getKategorie()).isEqualTo(UPDATED_KATEGORIE);
        assertThat(testAufgabe.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdateAufgabeWithPatch() throws Exception {
        // Initialize the database
        aufgabeRepository.saveAndFlush(aufgabe);

        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();

        // Update the aufgabe using partial update
        Aufgabe partialUpdatedAufgabe = new Aufgabe();
        partialUpdatedAufgabe.setId(aufgabe.getId());

        partialUpdatedAufgabe
            .name(UPDATED_NAME)
            .kurztext(UPDATED_KURZTEXT)
            .beschreibung(UPDATED_BESCHREIBUNG)
            .kategorie(UPDATED_KATEGORIE)
            .level(UPDATED_LEVEL);

        restAufgabeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAufgabe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAufgabe))
            )
            .andExpect(status().isOk());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
        Aufgabe testAufgabe = aufgabeList.get(aufgabeList.size() - 1);
        assertThat(testAufgabe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAufgabe.getKurztext()).isEqualTo(UPDATED_KURZTEXT);
        assertThat(testAufgabe.getBeschreibung()).isEqualTo(UPDATED_BESCHREIBUNG);
        assertThat(testAufgabe.getKategorie()).isEqualTo(UPDATED_KATEGORIE);
        assertThat(testAufgabe.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingAufgabe() throws Exception {
        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();
        aufgabe.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAufgabeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aufgabe.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aufgabe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAufgabe() throws Exception {
        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();
        aufgabe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAufgabeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aufgabe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAufgabe() throws Exception {
        int databaseSizeBeforeUpdate = aufgabeRepository.findAll().size();
        aufgabe.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAufgabeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aufgabe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Aufgabe in the database
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAufgabe() throws Exception {
        // Initialize the database
        aufgabeRepository.saveAndFlush(aufgabe);

        int databaseSizeBeforeDelete = aufgabeRepository.findAll().size();

        // Delete the aufgabe
        restAufgabeMockMvc
            .perform(delete(ENTITY_API_URL_ID, aufgabe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Aufgabe> aufgabeList = aufgabeRepository.findAll();
        assertThat(aufgabeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

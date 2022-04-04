package com.github.fwidder.drinkgame.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.github.fwidder.drinkgame.IntegrationTest;
import com.github.fwidder.drinkgame.domain.Spiel;
import com.github.fwidder.drinkgame.domain.Spieler;
import com.github.fwidder.drinkgame.repository.SpielerRepository;
import com.github.fwidder.drinkgame.service.SpielerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpielerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SpielerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/spielers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SpielerRepository spielerRepository;

    @Mock
    private SpielerRepository spielerRepositoryMock;

    @Mock
    private SpielerService spielerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpielerMockMvc;

    private Spieler spieler;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spieler createEntity(EntityManager em) {
        Spieler spieler = new Spieler().name(DEFAULT_NAME);
        // Add required entity
        Spiel spiel;
        if (TestUtil.findAll(em, Spiel.class).isEmpty()) {
            spiel = SpielResourceIT.createEntity(em);
            em.persist(spiel);
            em.flush();
        } else {
            spiel = TestUtil.findAll(em, Spiel.class).get(0);
        }
        spieler.setSpiel(spiel);
        return spieler;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Spieler createUpdatedEntity(EntityManager em) {
        Spieler spieler = new Spieler().name(UPDATED_NAME);
        // Add required entity
        Spiel spiel;
        if (TestUtil.findAll(em, Spiel.class).isEmpty()) {
            spiel = SpielResourceIT.createUpdatedEntity(em);
            em.persist(spiel);
            em.flush();
        } else {
            spiel = TestUtil.findAll(em, Spiel.class).get(0);
        }
        spieler.setSpiel(spiel);
        return spieler;
    }

    @BeforeEach
    public void initTest() {
        spieler = createEntity(em);
    }

    @Test
    @Transactional
    void createSpieler() throws Exception {
        int databaseSizeBeforeCreate = spielerRepository.findAll().size();
        // Create the Spieler
        restSpielerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spieler)))
            .andExpect(status().isCreated());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeCreate + 1);
        Spieler testSpieler = spielerList.get(spielerList.size() - 1);
        assertThat(testSpieler.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSpielerWithExistingId() throws Exception {
        // Create the Spieler with an existing ID
        spieler.setId(1L);

        int databaseSizeBeforeCreate = spielerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpielerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spieler)))
            .andExpect(status().isBadRequest());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = spielerRepository.findAll().size();
        // set the field null
        spieler.setName(null);

        // Create the Spieler, which fails.

        restSpielerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spieler)))
            .andExpect(status().isBadRequest());

        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpielers() throws Exception {
        // Initialize the database
        spielerRepository.saveAndFlush(spieler);

        // Get all the spielerList
        restSpielerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spieler.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpielersWithEagerRelationshipsIsEnabled() throws Exception {
        when(spielerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpielerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(spielerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSpielersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(spielerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSpielerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(spielerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSpieler() throws Exception {
        // Initialize the database
        spielerRepository.saveAndFlush(spieler);

        // Get the spieler
        restSpielerMockMvc
            .perform(get(ENTITY_API_URL_ID, spieler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(spieler.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSpieler() throws Exception {
        // Get the spieler
        restSpielerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSpieler() throws Exception {
        // Initialize the database
        spielerRepository.saveAndFlush(spieler);

        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();

        // Update the spieler
        Spieler updatedSpieler = spielerRepository.findById(spieler.getId()).get();
        // Disconnect from session so that the updates on updatedSpieler are not directly saved in db
        em.detach(updatedSpieler);
        updatedSpieler.name(UPDATED_NAME);

        restSpielerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSpieler.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSpieler))
            )
            .andExpect(status().isOk());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
        Spieler testSpieler = spielerList.get(spielerList.size() - 1);
        assertThat(testSpieler.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSpieler() throws Exception {
        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();
        spieler.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpielerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, spieler.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spieler))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpieler() throws Exception {
        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();
        spieler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpielerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(spieler))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpieler() throws Exception {
        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();
        spieler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpielerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(spieler)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpielerWithPatch() throws Exception {
        // Initialize the database
        spielerRepository.saveAndFlush(spieler);

        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();

        // Update the spieler using partial update
        Spieler partialUpdatedSpieler = new Spieler();
        partialUpdatedSpieler.setId(spieler.getId());

        restSpielerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpieler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpieler))
            )
            .andExpect(status().isOk());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
        Spieler testSpieler = spielerList.get(spielerList.size() - 1);
        assertThat(testSpieler.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSpielerWithPatch() throws Exception {
        // Initialize the database
        spielerRepository.saveAndFlush(spieler);

        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();

        // Update the spieler using partial update
        Spieler partialUpdatedSpieler = new Spieler();
        partialUpdatedSpieler.setId(spieler.getId());

        partialUpdatedSpieler.name(UPDATED_NAME);

        restSpielerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpieler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSpieler))
            )
            .andExpect(status().isOk());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
        Spieler testSpieler = spielerList.get(spielerList.size() - 1);
        assertThat(testSpieler.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSpieler() throws Exception {
        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();
        spieler.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpielerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, spieler.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spieler))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpieler() throws Exception {
        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();
        spieler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpielerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(spieler))
            )
            .andExpect(status().isBadRequest());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpieler() throws Exception {
        int databaseSizeBeforeUpdate = spielerRepository.findAll().size();
        spieler.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpielerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(spieler)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Spieler in the database
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpieler() throws Exception {
        // Initialize the database
        spielerRepository.saveAndFlush(spieler);

        int databaseSizeBeforeDelete = spielerRepository.findAll().size();

        // Delete the spieler
        restSpielerMockMvc
            .perform(delete(ENTITY_API_URL_ID, spieler.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Spieler> spielerList = spielerRepository.findAll();
        assertThat(spielerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

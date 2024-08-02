package com.kafka.consumer.web.rest;

import static com.kafka.consumer.domain.ProducerSyncAsserts.*;
import static com.kafka.consumer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.consumer.IntegrationTest;
import com.kafka.consumer.domain.ProducerSync;
import com.kafka.consumer.repository.ProducerSyncRepository;
import com.kafka.consumer.service.dto.ProducerSyncDTO;
import com.kafka.consumer.service.mapper.ProducerSyncMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProducerSyncResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProducerSyncResourceIT {

    private static final String DEFAULT_OWNER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/producer-syncs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProducerSyncRepository producerSyncRepository;

    @Autowired
    private ProducerSyncMapper producerSyncMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProducerSyncMockMvc;

    private ProducerSync producerSync;

    private ProducerSync insertedProducerSync;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProducerSync createEntity(EntityManager em) {
        ProducerSync producerSync = new ProducerSync()
            .ownerName(DEFAULT_OWNER_NAME)
            .producerName(DEFAULT_PRODUCER_NAME)
            .quantity(DEFAULT_QUANTITY);
        return producerSync;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProducerSync createUpdatedEntity(EntityManager em) {
        ProducerSync producerSync = new ProducerSync()
            .ownerName(UPDATED_OWNER_NAME)
            .producerName(UPDATED_PRODUCER_NAME)
            .quantity(UPDATED_QUANTITY);
        return producerSync;
    }

    @BeforeEach
    public void initTest() {
        producerSync = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedProducerSync != null) {
            producerSyncRepository.delete(insertedProducerSync);
            insertedProducerSync = null;
        }
    }

    @Test
    @Transactional
    void createProducerSync() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProducerSync
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(producerSync);
        var returnedProducerSyncDTO = om.readValue(
            restProducerSyncMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producerSyncDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProducerSyncDTO.class
        );

        // Validate the ProducerSync in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProducerSync = producerSyncMapper.toEntity(returnedProducerSyncDTO);
        assertProducerSyncUpdatableFieldsEquals(returnedProducerSync, getPersistedProducerSync(returnedProducerSync));

        insertedProducerSync = returnedProducerSync;
    }

    @Test
    @Transactional
    void createProducerSyncWithExistingId() throws Exception {
        // Create the ProducerSync with an existing ID
        producerSync.setId(1L);
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(producerSync);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProducerSyncMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producerSyncDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProducerSync in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProducerSyncs() throws Exception {
        // Initialize the database
        insertedProducerSync = producerSyncRepository.saveAndFlush(producerSync);

        // Get all the producerSyncList
        restProducerSyncMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producerSync.getId().intValue())))
            .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME)))
            .andExpect(jsonPath("$.[*].producerName").value(hasItem(DEFAULT_PRODUCER_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getProducerSync() throws Exception {
        // Initialize the database
        insertedProducerSync = producerSyncRepository.saveAndFlush(producerSync);

        // Get the producerSync
        restProducerSyncMockMvc
            .perform(get(ENTITY_API_URL_ID, producerSync.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producerSync.getId().intValue()))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME))
            .andExpect(jsonPath("$.producerName").value(DEFAULT_PRODUCER_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingProducerSync() throws Exception {
        // Get the producerSync
        restProducerSyncMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProducerSync() throws Exception {
        // Initialize the database
        insertedProducerSync = producerSyncRepository.saveAndFlush(producerSync);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producerSync
        ProducerSync updatedProducerSync = producerSyncRepository.findById(producerSync.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProducerSync are not directly saved in db
        em.detach(updatedProducerSync);
        updatedProducerSync.ownerName(UPDATED_OWNER_NAME).producerName(UPDATED_PRODUCER_NAME).quantity(UPDATED_QUANTITY);
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(updatedProducerSync);

        restProducerSyncMockMvc
            .perform(
                put(ENTITY_API_URL_ID, producerSyncDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(producerSyncDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProducerSync in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProducerSyncToMatchAllProperties(updatedProducerSync);
    }

    @Test
    @Transactional
    void putNonExistingProducerSync() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producerSync.setId(longCount.incrementAndGet());

        // Create the ProducerSync
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(producerSync);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerSyncMockMvc
            .perform(
                put(ENTITY_API_URL_ID, producerSyncDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(producerSyncDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProducerSync in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducerSync() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producerSync.setId(longCount.incrementAndGet());

        // Create the ProducerSync
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(producerSync);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerSyncMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(producerSyncDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProducerSync in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducerSync() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producerSync.setId(longCount.incrementAndGet());

        // Create the ProducerSync
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(producerSync);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerSyncMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(producerSyncDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProducerSync in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProducerSyncWithPatch() throws Exception {
        // Initialize the database
        insertedProducerSync = producerSyncRepository.saveAndFlush(producerSync);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producerSync using partial update
        ProducerSync partialUpdatedProducerSync = new ProducerSync();
        partialUpdatedProducerSync.setId(producerSync.getId());

        partialUpdatedProducerSync.ownerName(UPDATED_OWNER_NAME).producerName(UPDATED_PRODUCER_NAME).quantity(UPDATED_QUANTITY);

        restProducerSyncMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducerSync.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducerSync))
            )
            .andExpect(status().isOk());

        // Validate the ProducerSync in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProducerSyncUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProducerSync, producerSync),
            getPersistedProducerSync(producerSync)
        );
    }

    @Test
    @Transactional
    void fullUpdateProducerSyncWithPatch() throws Exception {
        // Initialize the database
        insertedProducerSync = producerSyncRepository.saveAndFlush(producerSync);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producerSync using partial update
        ProducerSync partialUpdatedProducerSync = new ProducerSync();
        partialUpdatedProducerSync.setId(producerSync.getId());

        partialUpdatedProducerSync.ownerName(UPDATED_OWNER_NAME).producerName(UPDATED_PRODUCER_NAME).quantity(UPDATED_QUANTITY);

        restProducerSyncMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducerSync.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducerSync))
            )
            .andExpect(status().isOk());

        // Validate the ProducerSync in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProducerSyncUpdatableFieldsEquals(partialUpdatedProducerSync, getPersistedProducerSync(partialUpdatedProducerSync));
    }

    @Test
    @Transactional
    void patchNonExistingProducerSync() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producerSync.setId(longCount.incrementAndGet());

        // Create the ProducerSync
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(producerSync);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerSyncMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, producerSyncDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(producerSyncDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProducerSync in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducerSync() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producerSync.setId(longCount.incrementAndGet());

        // Create the ProducerSync
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(producerSync);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerSyncMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(producerSyncDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProducerSync in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducerSync() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producerSync.setId(longCount.incrementAndGet());

        // Create the ProducerSync
        ProducerSyncDTO producerSyncDTO = producerSyncMapper.toDto(producerSync);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerSyncMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(producerSyncDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProducerSync in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducerSync() throws Exception {
        // Initialize the database
        insertedProducerSync = producerSyncRepository.saveAndFlush(producerSync);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the producerSync
        restProducerSyncMockMvc
            .perform(delete(ENTITY_API_URL_ID, producerSync.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return producerSyncRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProducerSync getPersistedProducerSync(ProducerSync producerSync) {
        return producerSyncRepository.findById(producerSync.getId()).orElseThrow();
    }

    protected void assertPersistedProducerSyncToMatchAllProperties(ProducerSync expectedProducerSync) {
        assertProducerSyncAllPropertiesEquals(expectedProducerSync, getPersistedProducerSync(expectedProducerSync));
    }

    protected void assertPersistedProducerSyncToMatchUpdatableProperties(ProducerSync expectedProducerSync) {
        assertProducerSyncAllUpdatablePropertiesEquals(expectedProducerSync, getPersistedProducerSync(expectedProducerSync));
    }
}

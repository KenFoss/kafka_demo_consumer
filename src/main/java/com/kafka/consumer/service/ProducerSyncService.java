package com.kafka.consumer.service;

import com.kafka.consumer.domain.ProducerSync;
import com.kafka.consumer.repository.ProducerSyncRepository;
import com.kafka.consumer.service.dto.ProducerSyncDTO;
import com.kafka.consumer.service.mapper.ProducerSyncMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kafka.consumer.domain.ProducerSync}.
 */
@Service
@Transactional
public class ProducerSyncService {

    private static final Logger log = LoggerFactory.getLogger(ProducerSyncService.class);

    private final ProducerSyncRepository producerSyncRepository;

    private final ProducerSyncMapper producerSyncMapper;

    public ProducerSyncService(ProducerSyncRepository producerSyncRepository, ProducerSyncMapper producerSyncMapper) {
        this.producerSyncRepository = producerSyncRepository;
        this.producerSyncMapper = producerSyncMapper;
    }

    /**
     * Save a producerSync.
     *
     * @param producerSyncDTO the entity to save.
     * @return the persisted entity.
     */
    public ProducerSyncDTO save(ProducerSyncDTO producerSyncDTO) {
        log.debug("Request to save ProducerSync : {}", producerSyncDTO);
        ProducerSync producerSync = producerSyncMapper.toEntity(producerSyncDTO);
        producerSync = producerSyncRepository.save(producerSync);
        return producerSyncMapper.toDto(producerSync);
    }

    /**
     * Update a producerSync.
     *
     * @param producerSyncDTO the entity to save.
     * @return the persisted entity.
     */
    public ProducerSyncDTO update(ProducerSyncDTO producerSyncDTO) {
        log.debug("Request to update ProducerSync : {}", producerSyncDTO);
        ProducerSync producerSync = producerSyncMapper.toEntity(producerSyncDTO);
        producerSync = producerSyncRepository.save(producerSync);
        return producerSyncMapper.toDto(producerSync);
    }

    /**
     * Partially update a producerSync.
     *
     * @param producerSyncDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProducerSyncDTO> partialUpdate(ProducerSyncDTO producerSyncDTO) {
        log.debug("Request to partially update ProducerSync : {}", producerSyncDTO);

        return producerSyncRepository
            .findById(producerSyncDTO.getId())
            .map(existingProducerSync -> {
                producerSyncMapper.partialUpdate(existingProducerSync, producerSyncDTO);

                return existingProducerSync;
            })
            .map(producerSyncRepository::save)
            .map(producerSyncMapper::toDto);
    }

    /**
     * Get all the producerSyncs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProducerSyncDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProducerSyncs");
        return producerSyncRepository.findAll(pageable).map(producerSyncMapper::toDto);
    }

    /**
     * Get one producerSync by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProducerSyncDTO> findOne(Long id) {
        log.debug("Request to get ProducerSync : {}", id);
        return producerSyncRepository.findById(id).map(producerSyncMapper::toDto);
    }

    /**
     * Delete the producerSync by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProducerSync : {}", id);
        producerSyncRepository.deleteById(id);
    }
}

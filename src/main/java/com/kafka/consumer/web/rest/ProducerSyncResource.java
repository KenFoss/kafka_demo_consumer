package com.kafka.consumer.web.rest;

import com.kafka.consumer.repository.ProducerSyncRepository;
import com.kafka.consumer.service.ProducerSyncService;
import com.kafka.consumer.service.dto.ProducerSyncDTO;
import com.kafka.consumer.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kafka.consumer.domain.ProducerSync}.
 */
@RestController
@RequestMapping("/api/producer-syncs")
public class ProducerSyncResource {

    private static final Logger log = LoggerFactory.getLogger(ProducerSyncResource.class);

    private static final String ENTITY_NAME = "kafkaDemoConsumerProducerSync";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProducerSyncService producerSyncService;

    private final ProducerSyncRepository producerSyncRepository;

    public ProducerSyncResource(ProducerSyncService producerSyncService, ProducerSyncRepository producerSyncRepository) {
        this.producerSyncService = producerSyncService;
        this.producerSyncRepository = producerSyncRepository;
    }

    /**
     * {@code POST  /producer-syncs} : Create a new producerSync.
     *
     * @param producerSyncDTO the producerSyncDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new producerSyncDTO, or with status {@code 400 (Bad Request)} if the producerSync has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProducerSyncDTO> createProducerSync(@RequestBody ProducerSyncDTO producerSyncDTO) throws URISyntaxException {
        log.debug("REST request to save ProducerSync : {}", producerSyncDTO);
        if (producerSyncDTO.getId() != null) {
            throw new BadRequestAlertException("A new producerSync cannot already have an ID", ENTITY_NAME, "idexists");
        }
        producerSyncDTO = producerSyncService.save(producerSyncDTO);
        return ResponseEntity.created(new URI("/api/producer-syncs/" + producerSyncDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, producerSyncDTO.getId().toString()))
            .body(producerSyncDTO);
    }

    /**
     * {@code PUT  /producer-syncs/:id} : Updates an existing producerSync.
     *
     * @param id the id of the producerSyncDTO to save.
     * @param producerSyncDTO the producerSyncDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated producerSyncDTO,
     * or with status {@code 400 (Bad Request)} if the producerSyncDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the producerSyncDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProducerSyncDTO> updateProducerSync(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProducerSyncDTO producerSyncDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProducerSync : {}, {}", id, producerSyncDTO);
        if (producerSyncDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, producerSyncDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!producerSyncRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        producerSyncDTO = producerSyncService.update(producerSyncDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, producerSyncDTO.getId().toString()))
            .body(producerSyncDTO);
    }

    /**
     * {@code PATCH  /producer-syncs/:id} : Partial updates given fields of an existing producerSync, field will ignore if it is null
     *
     * @param id the id of the producerSyncDTO to save.
     * @param producerSyncDTO the producerSyncDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated producerSyncDTO,
     * or with status {@code 400 (Bad Request)} if the producerSyncDTO is not valid,
     * or with status {@code 404 (Not Found)} if the producerSyncDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the producerSyncDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProducerSyncDTO> partialUpdateProducerSync(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ProducerSyncDTO producerSyncDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProducerSync partially : {}, {}", id, producerSyncDTO);
        if (producerSyncDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, producerSyncDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!producerSyncRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProducerSyncDTO> result = producerSyncService.partialUpdate(producerSyncDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, producerSyncDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /producer-syncs} : get all the producerSyncs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of producerSyncs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProducerSyncDTO>> getAllProducerSyncs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ProducerSyncs");
        Page<ProducerSyncDTO> page = producerSyncService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /producer-syncs/:id} : get the "id" producerSync.
     *
     * @param id the id of the producerSyncDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the producerSyncDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProducerSyncDTO> getProducerSync(@PathVariable("id") Long id) {
        log.debug("REST request to get ProducerSync : {}", id);
        Optional<ProducerSyncDTO> producerSyncDTO = producerSyncService.findOne(id);
        return ResponseUtil.wrapOrNotFound(producerSyncDTO);
    }

    /**
     * {@code DELETE  /producer-syncs/:id} : delete the "id" producerSync.
     *
     * @param id the id of the producerSyncDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducerSync(@PathVariable("id") Long id) {
        log.debug("REST request to delete ProducerSync : {}", id);
        producerSyncService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

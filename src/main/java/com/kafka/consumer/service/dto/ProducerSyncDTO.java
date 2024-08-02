package com.kafka.consumer.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.kafka.consumer.domain.ProducerSync} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProducerSyncDTO implements Serializable {

    private Long id;

    private String ownerName;

    private String productName;

    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String producerName) {
        this.productName = producerName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProducerSyncDTO)) {
            return false;
        }

        ProducerSyncDTO producerSyncDTO = (ProducerSyncDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, producerSyncDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProducerSyncDTO{" +
            "id=" + getId() +
            ", ownerName='" + getOwnerName() + "'" +
            ", producerName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}

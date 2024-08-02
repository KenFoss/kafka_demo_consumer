package com.kafka.consumer.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A ProducerSync.
 */
@Entity
@Table(name = "producer_sync")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProducerSync implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProducerSync id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public ProducerSync ownerName(String ownerName) {
        this.setOwnerName(ownerName);
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getProductName() {
        return this.productName;
    }

    public ProducerSync producerName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public ProducerSync quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProducerSync)) {
            return false;
        }
        return getId() != null && getId().equals(((ProducerSync) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProducerSync{" +
            "id=" + getId() +
            ", ownerName='" + getOwnerName() + "'" +
            ", producerName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            "}";
    }
}

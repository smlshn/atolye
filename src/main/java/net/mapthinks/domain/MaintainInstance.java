package net.mapthinks.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A MaintainInstance.
 */
@Entity
@Table(name = "maintain_instance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "maintaininstance")
public class MaintainInstance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "jhi_comment")
    private String comment;

    @Column(name = "price")
    private Double price;

    @Column(name = "price_second")
    private Double priceSecond;

    @ManyToOne
    private Employee doneBy;

    @ManyToOne
    private Operation operation;

    @ManyToOne
    private Maintenance maintenance;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public MaintainInstance name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public MaintainInstance comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getPrice() {
        return price;
    }

    public MaintainInstance price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceSecond() {
        return priceSecond;
    }

    public MaintainInstance priceSecond(Double priceSecond) {
        this.priceSecond = priceSecond;
        return this;
    }

    public void setPriceSecond(Double priceSecond) {
        this.priceSecond = priceSecond;
    }

    public Employee getDoneBy() {
        return doneBy;
    }

    public MaintainInstance doneBy(Employee employee) {
        this.doneBy = employee;
        return this;
    }

    public void setDoneBy(Employee employee) {
        this.doneBy = employee;
    }

    public Operation getOperation() {
        return operation;
    }

    public MaintainInstance operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public MaintainInstance maintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
        return this;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaintainInstance maintainInstance = (MaintainInstance) o;
        if (maintainInstance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), maintainInstance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaintainInstance{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", comment='" + getComment() + "'" +
            ", price='" + getPrice() + "'" +
            ", priceSecond='" + getPriceSecond() + "'" +
            "}";
    }
}

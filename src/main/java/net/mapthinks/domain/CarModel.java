package net.mapthinks.domain;

import net.mapthinks.domain.base.AbstractBaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CarModel.
 */
@Entity
@Table(name = "car_model")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "carmodel")
public class CarModel extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private CarBrand brand;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getName() {
        return name;
    }

    public CarModel name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarBrand getBrand() {
        return brand;
    }

    public CarModel brand(CarBrand carBrand) {
        this.brand = carBrand;
        return this;
    }

    public void setBrand(CarBrand carBrand) {
        this.brand = carBrand;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CarModel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

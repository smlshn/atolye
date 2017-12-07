package net.mapthinks.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Maintenance.
 */
@Entity
@Table(name = "maintenance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "maintenance")
public class Maintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "price_second")
    private Double priceSecond;

    @ManyToOne
    private Company company;

    @ManyToOne
    private MaintainType maintainType;

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

    public Maintenance name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public Maintenance price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPriceSecond() {
        return priceSecond;
    }

    public Maintenance priceSecond(Double priceSecond) {
        this.priceSecond = priceSecond;
        return this;
    }

    public void setPriceSecond(Double priceSecond) {
        this.priceSecond = priceSecond;
    }

    public Company getCompany() {
        return company;
    }

    public Maintenance company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public MaintainType getMaintainType() {
        return maintainType;
    }

    public Maintenance maintainType(MaintainType maintainType) {
        this.maintainType = maintainType;
        return this;
    }

    public void setMaintainType(MaintainType maintainType) {
        this.maintainType = maintainType;
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
        Maintenance maintenance = (Maintenance) o;
        if (maintenance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), maintenance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Maintenance{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", price='" + getPrice() + "'" +
            ", priceSecond='" + getPriceSecond() + "'" +
            "}";
    }
}

package net.mapthinks.domain;

import net.mapthinks.domain.base.AbstractBaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Operation.
 */
@Entity
@Table(name = "operation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "operation")
public class Operation extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "jhi_comment")
    private String comment;

    @Column(name = "price")
    private Double price;

    @Column(name = "discount_rate")
    private Double discountRate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "remainder")
    private Double remainder;

    @ManyToOne
    private Company company;

    @ManyToOne
    private Car car;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getComment() {
        return comment;
    }

    public Operation comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getPrice() {
        return price;
    }

    public Operation price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public Operation discountRate(Double discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Operation totalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getRemainder() {
        return remainder;
    }

    public Operation remainder(Double remainder) {
        this.remainder = remainder;
        return this;
    }

    public void setRemainder(Double remainder) {
        this.remainder = remainder;
    }

    public Company getCompany() {
        return company;
    }

    public Operation company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Car getCar() {
        return car;
    }

    public Operation car(Car car) {
        this.car = car;
        return this;
    }

    public void setCar(Car car) {
        this.car = car;
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
        return "Operation{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", price='" + getPrice() + "'" +
            ", discountRate='" + getDiscountRate() + "'" +
            ", totalPrice='" + getTotalPrice() + "'" +
            ", remainder='" + getRemainder() + "'" +
            "}";
    }
}

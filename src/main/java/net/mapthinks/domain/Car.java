package net.mapthinks.domain;

import net.mapthinks.domain.base.AbstractBaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "car")
public class Car extends AbstractBaseEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "plate", nullable = false)
    private String plate;

    @Column(name = "chase_no")
    private String chaseNo;

    @Column(name = "km")
    private Double km;

    @Column(name = "latest_oil_change_km")
    private Double latestOilChangeKm;

    @Column(name = "last_visit")
    private ZonedDateTime lastVisit;

    @ManyToOne
    private Company company;

    @ManyToOne
    private CarModel model;

    @ManyToOne
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getPlate() {
        return plate;
    }

    public Car plate(String plate) {
        this.plate = plate;
        return this;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getChaseNo() {
        return chaseNo;
    }

    public Car chaseNo(String chaseNo) {
        this.chaseNo = chaseNo;
        return this;
    }

    public void setChaseNo(String chaseNo) {
        this.chaseNo = chaseNo;
    }

    public Double getKm() {
        return km;
    }

    public Car km(Double km) {
        this.km = km;
        return this;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public Double getLatestOilChangeKm() {
        return latestOilChangeKm;
    }

    public Car latestOilChangeKm(Double latestOilChangeKm) {
        this.latestOilChangeKm = latestOilChangeKm;
        return this;
    }

    public void setLatestOilChangeKm(Double latestOilChangeKm) {
        this.latestOilChangeKm = latestOilChangeKm;
    }

    public ZonedDateTime getLastVisit() {
        return lastVisit;
    }

    public Car lastVisit(ZonedDateTime lastVisit) {
        this.lastVisit = lastVisit;
        return this;
    }

    public void setLastVisit(ZonedDateTime lastVisit) {
        this.lastVisit = lastVisit;
    }

    public Company getCompany() {
        return company;
    }

    public Car company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public CarModel getModel() {
        return model;
    }

    public Car model(CarModel carModel) {
        this.model = carModel;
        return this;
    }

    public void setModel(CarModel carModel) {
        this.model = carModel;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Car customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
        return "Car{" +
            "id=" + getId() +
            ", plate='" + getPlate() + "'" +
            ", chaseNo='" + getChaseNo() + "'" +
            ", km='" + getKm() + "'" +
            ", latestOilChangeKm='" + getLatestOilChangeKm() + "'" +
            ", lastVisit='" + getLastVisit() + "'" +
            "}";
    }
}

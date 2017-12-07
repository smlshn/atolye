package net.mapthinks.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "short_name", nullable = false)
    private String shortName;

    @Column(name = "vkn")
    private String vkn;

    @Column(name = "phone")
    private String phone;

    @Column(name = "phone_second")
    private String phoneSecond;

    @Column(name = "email")
    private String email;

    @Column(name = "email_second")
    private String emailSecond;

    @Column(name = "address")
    private String address;

    @Column(name = "address_second")
    private String addressSecond;

    @Column(name = "web_site")
    private String webSite;

    @ManyToOne
    private Company parent;

    @ManyToOne
    private Town town;

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

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public Company shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getVkn() {
        return vkn;
    }

    public Company vkn(String vkn) {
        this.vkn = vkn;
        return this;
    }

    public void setVkn(String vkn) {
        this.vkn = vkn;
    }

    public String getPhone() {
        return phone;
    }

    public Company phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneSecond() {
        return phoneSecond;
    }

    public Company phoneSecond(String phoneSecond) {
        this.phoneSecond = phoneSecond;
        return this;
    }

    public void setPhoneSecond(String phoneSecond) {
        this.phoneSecond = phoneSecond;
    }

    public String getEmail() {
        return email;
    }

    public Company email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailSecond() {
        return emailSecond;
    }

    public Company emailSecond(String emailSecond) {
        this.emailSecond = emailSecond;
        return this;
    }

    public void setEmailSecond(String emailSecond) {
        this.emailSecond = emailSecond;
    }

    public String getAddress() {
        return address;
    }

    public Company address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressSecond() {
        return addressSecond;
    }

    public Company addressSecond(String addressSecond) {
        this.addressSecond = addressSecond;
        return this;
    }

    public void setAddressSecond(String addressSecond) {
        this.addressSecond = addressSecond;
    }

    public String getWebSite() {
        return webSite;
    }

    public Company webSite(String webSite) {
        this.webSite = webSite;
        return this;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Company getParent() {
        return parent;
    }

    public Company parent(Company company) {
        this.parent = company;
        return this;
    }

    public void setParent(Company company) {
        this.parent = company;
    }

    public Town getTown() {
        return town;
    }

    public Company town(Town town) {
        this.town = town;
        return this;
    }

    public void setTown(Town town) {
        this.town = town;
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
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", vkn='" + getVkn() + "'" +
            ", phone='" + getPhone() + "'" +
            ", phoneSecond='" + getPhoneSecond() + "'" +
            ", email='" + getEmail() + "'" +
            ", emailSecond='" + getEmailSecond() + "'" +
            ", address='" + getAddress() + "'" +
            ", addressSecond='" + getAddressSecond() + "'" +
            ", webSite='" + getWebSite() + "'" +
            "}";
    }
}

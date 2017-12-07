package net.mapthinks.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customer")
public class Customer implements Serializable {

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

    @Column(name = "tckn")
    private String tckn;

    @Column(name = "phone")
    private String phone;

    @Column(name = "phone_second")
    private String phoneSecond;

    @Column(name = "email")
    private String email;

    @Column(name = "email_second")
    private String emailSecond;

    @Column(name = "web_site")
    private String webSite;

    @Column(name = "address")
    private String address;

    @Column(name = "address_second")
    private String addressSecond;

    @Column(name = "is_company")
    private Boolean isCompany;

    @ManyToOne
    private Company company;

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

    public Customer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public Customer shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getVkn() {
        return vkn;
    }

    public Customer vkn(String vkn) {
        this.vkn = vkn;
        return this;
    }

    public void setVkn(String vkn) {
        this.vkn = vkn;
    }

    public String getTckn() {
        return tckn;
    }

    public Customer tckn(String tckn) {
        this.tckn = tckn;
        return this;
    }

    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    public String getPhone() {
        return phone;
    }

    public Customer phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneSecond() {
        return phoneSecond;
    }

    public Customer phoneSecond(String phoneSecond) {
        this.phoneSecond = phoneSecond;
        return this;
    }

    public void setPhoneSecond(String phoneSecond) {
        this.phoneSecond = phoneSecond;
    }

    public String getEmail() {
        return email;
    }

    public Customer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailSecond() {
        return emailSecond;
    }

    public Customer emailSecond(String emailSecond) {
        this.emailSecond = emailSecond;
        return this;
    }

    public void setEmailSecond(String emailSecond) {
        this.emailSecond = emailSecond;
    }

    public String getWebSite() {
        return webSite;
    }

    public Customer webSite(String webSite) {
        this.webSite = webSite;
        return this;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getAddress() {
        return address;
    }

    public Customer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressSecond() {
        return addressSecond;
    }

    public Customer addressSecond(String addressSecond) {
        this.addressSecond = addressSecond;
        return this;
    }

    public void setAddressSecond(String addressSecond) {
        this.addressSecond = addressSecond;
    }

    public Boolean isIsCompany() {
        return isCompany;
    }

    public Customer isCompany(Boolean isCompany) {
        this.isCompany = isCompany;
        return this;
    }

    public void setIsCompany(Boolean isCompany) {
        this.isCompany = isCompany;
    }

    public Company getCompany() {
        return company;
    }

    public Customer company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Town getTown() {
        return town;
    }

    public Customer town(Town town) {
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
        Customer customer = (Customer) o;
        if (customer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", vkn='" + getVkn() + "'" +
            ", tckn='" + getTckn() + "'" +
            ", phone='" + getPhone() + "'" +
            ", phoneSecond='" + getPhoneSecond() + "'" +
            ", email='" + getEmail() + "'" +
            ", emailSecond='" + getEmailSecond() + "'" +
            ", webSite='" + getWebSite() + "'" +
            ", address='" + getAddress() + "'" +
            ", addressSecond='" + getAddressSecond() + "'" +
            ", isCompany='" + isIsCompany() + "'" +
            "}";
    }
}

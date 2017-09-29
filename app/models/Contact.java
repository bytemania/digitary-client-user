package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Entity
public class Contact {

    @Id
    @Column(name = "CONTACT_ID")
    @JsonIgnore
    private String id;

    @Constraints.Required(message = "user.validation.email.required")
    @Constraints.Email(message = "user.validation.email.invalid")
    private String email;
    @Constraints.Required(message = "user.validation.address1.required")
    private String address1;
    private String address2;
    @Constraints.Required(message = "user.validation.city.required")
    private String city;
    private String postalCode;
    @Constraints.Required(message = "user.validation.country")
    private String country;

    @Size(max = 3, message = "user.validation.phone.size")
    @ElementCollection(targetClass=String.class, fetch = FetchType.EAGER)
    private List<String> phones;

    public Contact() {
        id = UUID.randomUUID().toString();
    }

    public Contact(String email, String address1, String address2, String city, String postalCode, String country, List<String> phones) {
        id = UUID.randomUUID().toString();
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phones = phones;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public List<String> getPhones() {
        return phones;
    }
}

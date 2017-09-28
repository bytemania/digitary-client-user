package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "CONTACT_ID")
    private int id;

    @Constraints.Required
    private String email;
    @Constraints.Required
    private String address1;
    private String address2;
    @Constraints.Required
    private String city;
    private String postalCode;
    @Constraints.Required
    private String country;

    @Size(max = 3)
    @ElementCollection(targetClass=String.class, fetch = FetchType.EAGER)
    private List<String> phones;

    @JsonIgnore
    private Timestamp date;

    public Contact() {
        date = new Timestamp(new Date().getTime());
    }

    public Contact(String email, String address1, String address2, String city, String postalCode, String country, List<String> phones) {
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.phones = phones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

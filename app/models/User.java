package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class User {

    @Id
    @Constraints.Required(message = "user.validation.id.required")
    private Integer id;

    @Constraints.Required(message = "user.validation.name.required")
    private String name;

    @Valid
    @OneToOne(optional=false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="CONTACT_ID", unique=true, nullable=false, updatable=false)
    private Contact contact;

    @JsonIgnore
    private Timestamp date;

    public User() {
        this.date = new Timestamp(new Date().getTime());
    }

    public User(Integer id, String name, Contact contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.date = new Timestamp(new Date().getTime());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

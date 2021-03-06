package models;

import play.data.validation.Constraints;

import javax.persistence.*;
import javax.validation.Valid;

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


    public User(){;
    }

    public User(Integer id, String name, Contact contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
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

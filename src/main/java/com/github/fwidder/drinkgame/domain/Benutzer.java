package com.github.fwidder.drinkgame.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Benutzer.
 */
@Entity
@Table(name = "benutzer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Benutzer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "benutzer")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "benutzer", "spielers" }, allowSetters = true)
    private Set<Spiel> spiels = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Benutzer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Benutzer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Benutzer user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Spiel> getSpiels() {
        return this.spiels;
    }

    public void setSpiels(Set<Spiel> spiels) {
        if (this.spiels != null) {
            this.spiels.forEach(i -> i.setBenutzer(null));
        }
        if (spiels != null) {
            spiels.forEach(i -> i.setBenutzer(this));
        }
        this.spiels = spiels;
    }

    public Benutzer spiels(Set<Spiel> spiels) {
        this.setSpiels(spiels);
        return this;
    }

    public Benutzer addSpiel(Spiel spiel) {
        this.spiels.add(spiel);
        spiel.setBenutzer(this);
        return this;
    }

    public Benutzer removeSpiel(Spiel spiel) {
        this.spiels.remove(spiel);
        spiel.setBenutzer(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Benutzer)) {
            return false;
        }
        return id != null && id.equals(((Benutzer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Benutzer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

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
 * A Spieler.
 */
@Entity
@Table(name = "spieler")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Spieler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "benutzer", "spielers" }, allowSetters = true)
    private Spiel spiel;

    @ManyToMany
    @JoinTable(
        name = "rel_spieler__getraenk",
        joinColumns = @JoinColumn(name = "spieler_id"),
        inverseJoinColumns = @JoinColumn(name = "getraenk_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "spielers" }, allowSetters = true)
    private Set<Getraenk> getraenks = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_spieler__aufgabe",
        joinColumns = @JoinColumn(name = "spieler_id"),
        inverseJoinColumns = @JoinColumn(name = "aufgabe_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "spielers" }, allowSetters = true)
    private Set<Aufgabe> aufgabes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Spieler id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Spieler name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Spiel getSpiel() {
        return this.spiel;
    }

    public void setSpiel(Spiel spiel) {
        this.spiel = spiel;
    }

    public Spieler spiel(Spiel spiel) {
        this.setSpiel(spiel);
        return this;
    }

    public Set<Getraenk> getGetraenks() {
        return this.getraenks;
    }

    public void setGetraenks(Set<Getraenk> getraenks) {
        this.getraenks = getraenks;
    }

    public Spieler getraenks(Set<Getraenk> getraenks) {
        this.setGetraenks(getraenks);
        return this;
    }

    public Spieler addGetraenk(Getraenk getraenk) {
        this.getraenks.add(getraenk);
        getraenk.getSpielers().add(this);
        return this;
    }

    public Spieler removeGetraenk(Getraenk getraenk) {
        this.getraenks.remove(getraenk);
        getraenk.getSpielers().remove(this);
        return this;
    }

    public Set<Aufgabe> getAufgabes() {
        return this.aufgabes;
    }

    public void setAufgabes(Set<Aufgabe> aufgabes) {
        this.aufgabes = aufgabes;
    }

    public Spieler aufgabes(Set<Aufgabe> aufgabes) {
        this.setAufgabes(aufgabes);
        return this;
    }

    public Spieler addAufgabe(Aufgabe aufgabe) {
        this.aufgabes.add(aufgabe);
        aufgabe.getSpielers().add(this);
        return this;
    }

    public Spieler removeAufgabe(Aufgabe aufgabe) {
        this.aufgabes.remove(aufgabe);
        aufgabe.getSpielers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spieler)) {
            return false;
        }
        return id != null && id.equals(((Spieler) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Spieler{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

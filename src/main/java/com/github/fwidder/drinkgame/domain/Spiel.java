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
 * A Spiel.
 */
@Entity
@Table(name = "spiel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Spiel implements Serializable {

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
    @JsonIgnoreProperties(value = { "user", "spiels" }, allowSetters = true)
    private Benutzer benutzer;

    @OneToMany(mappedBy = "spiel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "spiel", "getraenks", "aufgabes" }, allowSetters = true)
    private Set<Spieler> spielers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Spiel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Spiel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Benutzer getBenutzer() {
        return this.benutzer;
    }

    public void setBenutzer(Benutzer benutzer) {
        this.benutzer = benutzer;
    }

    public Spiel benutzer(Benutzer benutzer) {
        this.setBenutzer(benutzer);
        return this;
    }

    public Set<Spieler> getSpielers() {
        return this.spielers;
    }

    public void setSpielers(Set<Spieler> spielers) {
        if (this.spielers != null) {
            this.spielers.forEach(i -> i.setSpiel(null));
        }
        if (spielers != null) {
            spielers.forEach(i -> i.setSpiel(this));
        }
        this.spielers = spielers;
    }

    public Spiel spielers(Set<Spieler> spielers) {
        this.setSpielers(spielers);
        return this;
    }

    public Spiel addSpieler(Spieler spieler) {
        this.spielers.add(spieler);
        spieler.setSpiel(this);
        return this;
    }

    public Spiel removeSpieler(Spieler spieler) {
        this.spielers.remove(spieler);
        spieler.setSpiel(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Spiel)) {
            return false;
        }
        return id != null && id.equals(((Spiel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Spiel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

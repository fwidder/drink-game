package com.github.fwidder.drinkgame.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.fwidder.drinkgame.domain.enumeration.Groesse;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Getraenk.
 */
@Entity
@Table(name = "getraenk")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Getraenk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "groesse", nullable = false)
    private Groesse groesse;

    @ManyToMany(mappedBy = "getraenks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "spiel", "getraenks", "aufgabes" }, allowSetters = true)
    private Set<Spieler> spielers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Getraenk id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Getraenk name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Groesse getGroesse() {
        return this.groesse;
    }

    public Getraenk groesse(Groesse groesse) {
        this.setGroesse(groesse);
        return this;
    }

    public void setGroesse(Groesse groesse) {
        this.groesse = groesse;
    }

    public Set<Spieler> getSpielers() {
        return this.spielers;
    }

    public void setSpielers(Set<Spieler> spielers) {
        if (this.spielers != null) {
            this.spielers.forEach(i -> i.removeGetraenk(this));
        }
        if (spielers != null) {
            spielers.forEach(i -> i.addGetraenk(this));
        }
        this.spielers = spielers;
    }

    public Getraenk spielers(Set<Spieler> spielers) {
        this.setSpielers(spielers);
        return this;
    }

    public Getraenk addSpieler(Spieler spieler) {
        this.spielers.add(spieler);
        spieler.getGetraenks().add(this);
        return this;
    }

    public Getraenk removeSpieler(Spieler spieler) {
        this.spielers.remove(spieler);
        spieler.getGetraenks().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Getraenk)) {
            return false;
        }
        return id != null && id.equals(((Getraenk) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Getraenk{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", groesse='" + getGroesse() + "'" +
            "}";
    }
}

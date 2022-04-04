package com.github.fwidder.drinkgame.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.fwidder.drinkgame.domain.enumeration.Kategorie;
import com.github.fwidder.drinkgame.domain.enumeration.Level;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Aufgabe.
 */
@Entity
@Table(name = "aufgabe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Aufgabe implements Serializable {

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
    @Column(name = "kurztext", nullable = false)
    private String kurztext;

    @NotNull
    @Column(name = "beschreibung", nullable = false)
    private String beschreibung;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "kategorie", nullable = false)
    private Kategorie kategorie;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @ManyToMany(mappedBy = "aufgabes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "spiel", "getraenks", "aufgabes" }, allowSetters = true)
    private Set<Spieler> spielers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Aufgabe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Aufgabe name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKurztext() {
        return this.kurztext;
    }

    public Aufgabe kurztext(String kurztext) {
        this.setKurztext(kurztext);
        return this;
    }

    public void setKurztext(String kurztext) {
        this.kurztext = kurztext;
    }

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public Aufgabe beschreibung(String beschreibung) {
        this.setBeschreibung(beschreibung);
        return this;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Kategorie getKategorie() {
        return this.kategorie;
    }

    public Aufgabe kategorie(Kategorie kategorie) {
        this.setKategorie(kategorie);
        return this;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    public Level getLevel() {
        return this.level;
    }

    public Aufgabe level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Set<Spieler> getSpielers() {
        return this.spielers;
    }

    public void setSpielers(Set<Spieler> spielers) {
        if (this.spielers != null) {
            this.spielers.forEach(i -> i.removeAufgabe(this));
        }
        if (spielers != null) {
            spielers.forEach(i -> i.addAufgabe(this));
        }
        this.spielers = spielers;
    }

    public Aufgabe spielers(Set<Spieler> spielers) {
        this.setSpielers(spielers);
        return this;
    }

    public Aufgabe addSpieler(Spieler spieler) {
        this.spielers.add(spieler);
        spieler.getAufgabes().add(this);
        return this;
    }

    public Aufgabe removeSpieler(Spieler spieler) {
        this.spielers.remove(spieler);
        spieler.getAufgabes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Aufgabe)) {
            return false;
        }
        return id != null && id.equals(((Aufgabe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Aufgabe{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", kurztext='" + getKurztext() + "'" +
            ", beschreibung='" + getBeschreibung() + "'" +
            ", kategorie='" + getKategorie() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}

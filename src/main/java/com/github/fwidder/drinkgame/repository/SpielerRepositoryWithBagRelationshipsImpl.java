package com.github.fwidder.drinkgame.repository;

import com.github.fwidder.drinkgame.domain.Spieler;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SpielerRepositoryWithBagRelationshipsImpl implements SpielerRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Spieler> fetchBagRelationships(Optional<Spieler> spieler) {
        return spieler.map(this::fetchGetraenks).map(this::fetchAufgabes);
    }

    @Override
    public Page<Spieler> fetchBagRelationships(Page<Spieler> spielers) {
        return new PageImpl<>(fetchBagRelationships(spielers.getContent()), spielers.getPageable(), spielers.getTotalElements());
    }

    @Override
    public List<Spieler> fetchBagRelationships(List<Spieler> spielers) {
        return Optional.of(spielers).map(this::fetchGetraenks).map(this::fetchAufgabes).orElse(Collections.emptyList());
    }

    Spieler fetchGetraenks(Spieler result) {
        return entityManager
            .createQuery("select spieler from Spieler spieler left join fetch spieler.getraenks where spieler is :spieler", Spieler.class)
            .setParameter("spieler", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Spieler> fetchGetraenks(List<Spieler> spielers) {
        return entityManager
            .createQuery(
                "select distinct spieler from Spieler spieler left join fetch spieler.getraenks where spieler in :spielers",
                Spieler.class
            )
            .setParameter("spielers", spielers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Spieler fetchAufgabes(Spieler result) {
        return entityManager
            .createQuery("select spieler from Spieler spieler left join fetch spieler.aufgabes where spieler is :spieler", Spieler.class)
            .setParameter("spieler", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Spieler> fetchAufgabes(List<Spieler> spielers) {
        return entityManager
            .createQuery(
                "select distinct spieler from Spieler spieler left join fetch spieler.aufgabes where spieler in :spielers",
                Spieler.class
            )
            .setParameter("spielers", spielers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}

package com.github.fwidder.drinkgame.repository;

import com.github.fwidder.drinkgame.domain.Spieler;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Spieler entity.
 */
@Repository
public interface SpielerRepository extends SpielerRepositoryWithBagRelationships, JpaRepository<Spieler, Long> {
    default Optional<Spieler> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Spieler> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Spieler> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}

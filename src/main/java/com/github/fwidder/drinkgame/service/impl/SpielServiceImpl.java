package com.github.fwidder.drinkgame.service.impl;

import com.github.fwidder.drinkgame.domain.Spiel;
import com.github.fwidder.drinkgame.repository.SpielRepository;
import com.github.fwidder.drinkgame.service.SpielService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Spiel}.
 */
@Service
@Transactional
public class SpielServiceImpl implements SpielService {

    private final Logger log = LoggerFactory.getLogger(SpielServiceImpl.class);

    private final SpielRepository spielRepository;

    public SpielServiceImpl(SpielRepository spielRepository) {
        this.spielRepository = spielRepository;
    }

    @Override
    public Spiel save(Spiel spiel) {
        log.debug("Request to save Spiel : {}", spiel);
        return spielRepository.save(spiel);
    }

    @Override
    public Spiel update(Spiel spiel) {
        log.debug("Request to save Spiel : {}", spiel);
        return spielRepository.save(spiel);
    }

    @Override
    public Optional<Spiel> partialUpdate(Spiel spiel) {
        log.debug("Request to partially update Spiel : {}", spiel);

        return spielRepository
            .findById(spiel.getId())
            .map(existingSpiel -> {
                if (spiel.getName() != null) {
                    existingSpiel.setName(spiel.getName());
                }

                return existingSpiel;
            })
            .map(spielRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Spiel> findAll(Pageable pageable) {
        log.debug("Request to get all Spiels");
        return spielRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Spiel> findOne(Long id) {
        log.debug("Request to get Spiel : {}", id);
        return spielRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Spiel : {}", id);
        spielRepository.deleteById(id);
    }
}

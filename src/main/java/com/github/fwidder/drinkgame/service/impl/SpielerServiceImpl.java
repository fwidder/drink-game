package com.github.fwidder.drinkgame.service.impl;

import com.github.fwidder.drinkgame.domain.Spieler;
import com.github.fwidder.drinkgame.repository.SpielerRepository;
import com.github.fwidder.drinkgame.service.SpielerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Spieler}.
 */
@Service
@Transactional
public class SpielerServiceImpl implements SpielerService {

    private final Logger log = LoggerFactory.getLogger(SpielerServiceImpl.class);

    private final SpielerRepository spielerRepository;

    public SpielerServiceImpl(SpielerRepository spielerRepository) {
        this.spielerRepository = spielerRepository;
    }

    @Override
    public Spieler save(Spieler spieler) {
        log.debug("Request to save Spieler : {}", spieler);
        return spielerRepository.save(spieler);
    }

    @Override
    public Spieler update(Spieler spieler) {
        log.debug("Request to save Spieler : {}", spieler);
        return spielerRepository.save(spieler);
    }

    @Override
    public Optional<Spieler> partialUpdate(Spieler spieler) {
        log.debug("Request to partially update Spieler : {}", spieler);

        return spielerRepository
            .findById(spieler.getId())
            .map(existingSpieler -> {
                if (spieler.getName() != null) {
                    existingSpieler.setName(spieler.getName());
                }

                return existingSpieler;
            })
            .map(spielerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Spieler> findAll(Pageable pageable) {
        log.debug("Request to get all Spielers");
        return spielerRepository.findAll(pageable);
    }

    public Page<Spieler> findAllWithEagerRelationships(Pageable pageable) {
        return spielerRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Spieler> findOne(Long id) {
        log.debug("Request to get Spieler : {}", id);
        return spielerRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Spieler : {}", id);
        spielerRepository.deleteById(id);
    }
}

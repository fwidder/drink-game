package com.github.fwidder.drinkgame.service.impl;

import com.github.fwidder.drinkgame.domain.Benutzer;
import com.github.fwidder.drinkgame.repository.BenutzerRepository;
import com.github.fwidder.drinkgame.service.BenutzerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Benutzer}.
 */
@Service
@Transactional
public class BenutzerServiceImpl implements BenutzerService {

    private final Logger log = LoggerFactory.getLogger(BenutzerServiceImpl.class);

    private final BenutzerRepository benutzerRepository;

    public BenutzerServiceImpl(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    @Override
    public Benutzer save(Benutzer benutzer) {
        log.debug("Request to save Benutzer : {}", benutzer);
        return benutzerRepository.save(benutzer);
    }

    @Override
    public Benutzer update(Benutzer benutzer) {
        log.debug("Request to save Benutzer : {}", benutzer);
        return benutzerRepository.save(benutzer);
    }

    @Override
    public Optional<Benutzer> partialUpdate(Benutzer benutzer) {
        log.debug("Request to partially update Benutzer : {}", benutzer);

        return benutzerRepository
            .findById(benutzer.getId())
            .map(existingBenutzer -> {
                if (benutzer.getName() != null) {
                    existingBenutzer.setName(benutzer.getName());
                }

                return existingBenutzer;
            })
            .map(benutzerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Benutzer> findAll(Pageable pageable) {
        log.debug("Request to get all Benutzers");
        return benutzerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Benutzer> findOne(Long id) {
        log.debug("Request to get Benutzer : {}", id);
        return benutzerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Benutzer : {}", id);
        benutzerRepository.deleteById(id);
    }
}

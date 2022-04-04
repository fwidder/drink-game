package com.github.fwidder.drinkgame.service.impl;

import com.github.fwidder.drinkgame.domain.Aufgabe;
import com.github.fwidder.drinkgame.repository.AufgabeRepository;
import com.github.fwidder.drinkgame.service.AufgabeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Aufgabe}.
 */
@Service
@Transactional
public class AufgabeServiceImpl implements AufgabeService {

    private final Logger log = LoggerFactory.getLogger(AufgabeServiceImpl.class);

    private final AufgabeRepository aufgabeRepository;

    public AufgabeServiceImpl(AufgabeRepository aufgabeRepository) {
        this.aufgabeRepository = aufgabeRepository;
    }

    @Override
    public Aufgabe save(Aufgabe aufgabe) {
        log.debug("Request to save Aufgabe : {}", aufgabe);
        return aufgabeRepository.save(aufgabe);
    }

    @Override
    public Aufgabe update(Aufgabe aufgabe) {
        log.debug("Request to save Aufgabe : {}", aufgabe);
        return aufgabeRepository.save(aufgabe);
    }

    @Override
    public Optional<Aufgabe> partialUpdate(Aufgabe aufgabe) {
        log.debug("Request to partially update Aufgabe : {}", aufgabe);

        return aufgabeRepository
            .findById(aufgabe.getId())
            .map(existingAufgabe -> {
                if (aufgabe.getName() != null) {
                    existingAufgabe.setName(aufgabe.getName());
                }
                if (aufgabe.getKurztext() != null) {
                    existingAufgabe.setKurztext(aufgabe.getKurztext());
                }
                if (aufgabe.getBeschreibung() != null) {
                    existingAufgabe.setBeschreibung(aufgabe.getBeschreibung());
                }
                if (aufgabe.getKategorie() != null) {
                    existingAufgabe.setKategorie(aufgabe.getKategorie());
                }
                if (aufgabe.getLevel() != null) {
                    existingAufgabe.setLevel(aufgabe.getLevel());
                }

                return existingAufgabe;
            })
            .map(aufgabeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Aufgabe> findAll(Pageable pageable) {
        log.debug("Request to get all Aufgabes");
        return aufgabeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Aufgabe> findOne(Long id) {
        log.debug("Request to get Aufgabe : {}", id);
        return aufgabeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Aufgabe : {}", id);
        aufgabeRepository.deleteById(id);
    }
}

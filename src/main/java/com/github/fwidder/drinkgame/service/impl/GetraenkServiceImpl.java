package com.github.fwidder.drinkgame.service.impl;

import com.github.fwidder.drinkgame.domain.Getraenk;
import com.github.fwidder.drinkgame.repository.GetraenkRepository;
import com.github.fwidder.drinkgame.service.GetraenkService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Getraenk}.
 */
@Service
@Transactional
public class GetraenkServiceImpl implements GetraenkService {

    private final Logger log = LoggerFactory.getLogger(GetraenkServiceImpl.class);

    private final GetraenkRepository getraenkRepository;

    public GetraenkServiceImpl(GetraenkRepository getraenkRepository) {
        this.getraenkRepository = getraenkRepository;
    }

    @Override
    public Getraenk save(Getraenk getraenk) {
        log.debug("Request to save Getraenk : {}", getraenk);
        return getraenkRepository.save(getraenk);
    }

    @Override
    public Getraenk update(Getraenk getraenk) {
        log.debug("Request to save Getraenk : {}", getraenk);
        return getraenkRepository.save(getraenk);
    }

    @Override
    public Optional<Getraenk> partialUpdate(Getraenk getraenk) {
        log.debug("Request to partially update Getraenk : {}", getraenk);

        return getraenkRepository
            .findById(getraenk.getId())
            .map(existingGetraenk -> {
                if (getraenk.getName() != null) {
                    existingGetraenk.setName(getraenk.getName());
                }
                if (getraenk.getGroesse() != null) {
                    existingGetraenk.setGroesse(getraenk.getGroesse());
                }

                return existingGetraenk;
            })
            .map(getraenkRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Getraenk> findAll(Pageable pageable) {
        log.debug("Request to get all Getraenks");
        return getraenkRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Getraenk> findOne(Long id) {
        log.debug("Request to get Getraenk : {}", id);
        return getraenkRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Getraenk : {}", id);
        getraenkRepository.deleteById(id);
    }
}

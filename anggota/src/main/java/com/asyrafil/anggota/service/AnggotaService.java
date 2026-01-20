package com.asyrafil.anggota.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.asyrafil.anggota.model.Anggota;
import com.asyrafil.anggota.repository.AnggotaRepository;

import static net.logstash.logback.marker.Markers.append;

@Service
public class AnggotaService {

    private static final Logger logger = LoggerFactory.getLogger(AnggotaService.class);

    @Autowired
    private AnggotaRepository anggotaRepository;

    public List<Anggota> getAllAnggotas() {
        logger.info("Fetching all anggota");
        List<Anggota> anggotas = anggotaRepository.findAll();
        logger.info(append("event", "anggota_fetched_all")
                .and(append("count", anggotas.size())),
                "Retrieved {} anggota records", anggotas.size());
        return anggotas;
    }

    public Anggota getAnggotaById(Long id) {
        logger.info(append("event", "anggota_fetch_by_id")
                .and(append("anggota_id", id)),
                "Fetching anggota with ID: {}", id);
        Anggota anggota = anggotaRepository.findById(id).orElse(null);
        if (anggota != null) {
            logger.info(append("event", "anggota_found")
                    .and(append("anggota_id", id))
                    .and(append("nama", anggota.getNama())),
                    "Found anggota: {}", anggota.getNama());
        } else {
            logger.warn(append("event", "anggota_not_found")
                    .and(append("anggota_id", id)),
                    "Anggota with ID {} not found", id);
        }
        return anggota;
    }

    public Anggota createAnggota(Anggota anggota) {
        logger.info(append("event", "anggota_create_request")
                .and(append("nama", anggota.getNama()))
                .and(append("email", anggota.getEmail())),
                "Creating new anggota: {}", anggota.getNama());

        Anggota savedAnggota = anggotaRepository.save(anggota);

        logger.info(append("event", "anggota_created")
                .and(append("anggota_id", savedAnggota.getId()))
                .and(append("nama", savedAnggota.getNama()))
                .and(append("email", savedAnggota.getEmail())),
                "Anggota berhasil ditambahkan dengan ID: {}", savedAnggota.getId());

        return savedAnggota;
    }

    public void deleteAnggota(Long id) {
        logger.info(append("event", "anggota_delete_request")
                .and(append("anggota_id", id)),
                "Deleting anggota with ID: {}", id);

        anggotaRepository.deleteById(id);

        logger.info(append("event", "anggota_deleted")
                .and(append("anggota_id", id)),
                "Successfully deleted anggota with ID: {}", id);
    }
}

package com.asyrafil.buku.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.asyrafil.buku.model.Buku;
import com.asyrafil.buku.repository.BukuRepository;

import static net.logstash.logback.marker.Markers.append;

@Service
public class BukuService {

    private static final Logger logger = LoggerFactory.getLogger(BukuService.class);

    @Autowired
    private BukuRepository bukuRepository;

    public List<Buku> getAllBukus() {
        logger.info("Fetching all buku");
        List<Buku> bukus = bukuRepository.findAll();
        logger.info(append("event", "buku_fetched_all")
                .and(append("count", bukus.size())),
                "Retrieved {} buku records", bukus.size());
        return bukus;
    }

    public Buku getBukuById(Long id) {
        logger.info(append("event", "buku_fetch_by_id")
                .and(append("buku_id", id)),
                "Fetching buku with ID: {}", id);
        Buku buku = bukuRepository.findById(id).orElse(null);
        if (buku != null) {
            logger.info(append("event", "buku_found")
                    .and(append("buku_id", id))
                    .and(append("judul", buku.getJudul()))
                    .and(append("pengarang", buku.getPengarang())),
                    "Found buku: {} by {}", buku.getJudul(), buku.getPengarang());
        } else {
            logger.warn(append("event", "buku_not_found")
                    .and(append("buku_id", id)),
                    "Buku with ID {} not found", id);
        }
        return buku;
    }

    public Buku createBuku(Buku buku) {
        logger.info(append("event", "buku_create_request")
                .and(append("judul", buku.getJudul()))
                .and(append("pengarang", buku.getPengarang())),
                "Creating new buku: {} by {}", buku.getJudul(), buku.getPengarang());

        Buku savedBuku = bukuRepository.save(buku);

        logger.info(append("event", "buku_created")
                .and(append("buku_id", savedBuku.getId()))
                .and(append("judul", savedBuku.getJudul()))
                .and(append("pengarang", savedBuku.getPengarang())),
                "Successfully created buku with ID: {}", savedBuku.getId());

        return savedBuku;
    }

    public void deleteBuku(Long id) {
        logger.info(append("event", "buku_delete_request")
                .and(append("buku_id", id)),
                "Deleting buku with ID: {}", id);

        bukuRepository.deleteById(id);

        logger.info(append("event", "buku_deleted")
                .and(append("buku_id", id)),
                "Successfully deleted buku with ID: {}", id);
    }

}

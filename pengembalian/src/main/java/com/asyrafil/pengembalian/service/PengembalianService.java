package com.asyrafil.pengembalian.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.asyrafil.pengembalian.model.Pengembalian;
import com.asyrafil.pengembalian.repository.PengembalianRepository;
import com.asyrafil.pengembalian.vo.Peminjaman;
import com.asyrafil.pengembalian.vo.ResponseTemplate;
import com.asyrafil.pengembalian.email.EmailProducer;

import static net.logstash.logback.marker.Markers.append;

@Service
public class PengembalianService {

    private static final Logger logger = LoggerFactory.getLogger(PengembalianService.class);

    private final EmailProducer emailProducer;

    public PengembalianService(EmailProducer emailProducer) {
        this.emailProducer = emailProducer;
    }

    @Autowired
    PengembalianRepository pengembalianRepository;

    public List<Pengembalian> getAllPengembalians() {
        logger.info("Fetching all pengembalian");
        List<Pengembalian> pengembalians = pengembalianRepository.findAll();
        logger.info(append("event", "pengembalian_fetched_all")
                .and(append("count", pengembalians.size())),
                "Retrieved {} pengembalian records", pengembalians.size());
        return pengembalians;
    }

    public Pengembalian getPengembalianById(Long id) {
        logger.info(append("event", "pengembalian_fetch_by_id")
                .and(append("pengembalian_id", id)),
                "Fetching pengembalian with ID: {}", id);
        Pengembalian pengembalian = pengembalianRepository.findById(id).orElse(null);
        if (pengembalian != null) {
            logger.info(append("event", "pengembalian_found")
                    .and(append("pengembalian_id", id))
                    .and(append("peminjaman_id", pengembalian.getPeminjaman_id()))
                    .and(append("denda", pengembalian.getDenda())),
                    "Found pengembalian for peminjaman ID: {} with denda: {}",
                    pengembalian.getPeminjaman_id(), pengembalian.getDenda());
        } else {
            logger.warn(append("event", "pengembalian_not_found")
                    .and(append("pengembalian_id", id)),
                    "Pengembalian with ID {} not found", id);
        }
        return pengembalian;
    }

    public Pengembalian createPengembalian(Pengembalian pengembalian) {
        logger.info(append("event", "pengembalian_create_request")
                .and(append("peminjaman_id", pengembalian.getPeminjaman_id()))
                .and(append("denda", pengembalian.getDenda())),
                "Creating pengembalian for peminjaman ID: {} with denda: {}",
                pengembalian.getPeminjaman_id(), pengembalian.getDenda());

        Pengembalian savedPengembalian = pengembalianRepository.save(pengembalian);

        logger.info(append("event", "pengembalian_created")
                .and(append("pengembalian_id", savedPengembalian.getId()))
                .and(append("peminjaman_id", savedPengembalian.getPeminjaman_id()))
                .and(append("denda", savedPengembalian.getDenda())),
                "Berhasil menambahkan pengembalian dengan ID: {}", savedPengembalian.getId());

        return savedPengembalian;
    }

    public void deletePengembalian(Long id) {
        logger.info(append("event", "pengembalian_delete_request")
                .and(append("pengembalian_id", id)),
                "Deleting pengembalian with ID: {}", id);

        pengembalianRepository.deleteById(id);

        logger.info(append("event", "pengembalian_deleted")
                .and(append("pengembalian_id", id)),
                "Successfully deleted pengembalian with ID: {}", id);
    }

    public void prosesPengembalian(String emailPeminjam, String namaBuku) {
        String subject = "Pengembalian Buku Berhasil";
        String body = "Terima kasih telah mengembalikan buku \"" + namaBuku + "\".";

        emailProducer.sendEmail(emailPeminjam, subject, body);
    }

    @Autowired
    private RestTemplate restTemplate;

    public List<ResponseTemplate> getPengembalianWithPeminjamanById(Long id) {
        List<ResponseTemplate> responseList = new ArrayList<>();
        Pengembalian pengembalian = getPengembalianById(id);
        Peminjaman peminjaman = restTemplate
                .getForObject("http://PEMINJAMAN/api/peminjaman/" + pengembalian.getPeminjaman_id(), Peminjaman.class);
        ResponseTemplate vo = new ResponseTemplate();
        vo.setPeminjaman(peminjaman);
        vo.setPengembalian(pengembalian);
        responseList.add(vo);
        return responseList;
    }

}

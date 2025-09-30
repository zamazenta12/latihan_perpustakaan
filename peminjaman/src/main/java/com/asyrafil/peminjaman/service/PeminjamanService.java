package com.asyrafil.peminjaman.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.asyrafil.peminjaman.model.Peminjaman;
import com.asyrafil.peminjaman.repository.PeminjamanRepository;
import com.asyrafil.peminjaman.vo.Anggota;
import com.asyrafil.peminjaman.vo.Buku;
import com.asyrafil.peminjaman.vo.ResponseTemplate;

@Service
public class PeminjamanService {
    @Autowired
    PeminjamanRepository peminjamanRepository;

    public List<Peminjaman> getAllPeminjamans() {
        return peminjamanRepository.findAll();
    }

    public Peminjaman getPeminjamanById(Long id) {
        return peminjamanRepository.findById(id).orElse(null);
    }

    public Peminjaman createPeminjaman(Peminjaman peminjaman) {
        return peminjamanRepository.save(peminjaman);
    }

    public void deletePeminjaman(Long id) {
        peminjamanRepository.deleteById(id);
    }

    @Autowired
    private RestTemplate restTemplate;
    public List<ResponseTemplate> getPeminjamanWithBukuById(Long id){
        List<ResponseTemplate> responseList = new ArrayList<>();
        Peminjaman peminjaman = getPeminjamanById(id);
        Buku buku = restTemplate.getForObject("http://localhost:8082/api/buku/"
                + peminjaman.getBuku_id(), Buku.class);
        Anggota anggota = restTemplate.getForObject("http://localhost:8081/api/anggota/"
                + peminjaman.getAnggota_id(), Anggota.class);
        ResponseTemplate vo = new ResponseTemplate();
        vo.setPeminjaman(peminjaman);
        vo.setBuku(buku);
        vo.setAnggota(anggota);
        responseList.add(vo);
        return responseList;
    }

}


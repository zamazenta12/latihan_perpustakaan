package com.asyrafil.pengembalian.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.asyrafil.pengembalian.model.Pengembalian;
import com.asyrafil.pengembalian.repository.PengembalianRepository;
import com.asyrafil.pengembalian.vo.Peminjaman;
import com.asyrafil.pengembalian.vo.ResponseTemplate;

@Service
public class PengembalianService {

    @Autowired
    PengembalianRepository pengembalianRepository;

    public List<Pengembalian> getAllPengembalians() {
        return pengembalianRepository.findAll();
    }

    public Pengembalian getPengembalianById(Long id) {
        return pengembalianRepository.findById(id).orElse(null);
    }

    public Pengembalian createPengembalian(Pengembalian pengembalian) {
        return pengembalianRepository.save(pengembalian);
    }

    public void deletePengembalian(Long id) {
        pengembalianRepository.deleteById(id);
    }

    @Autowired
    private RestTemplate restTemplate;
    public List<ResponseTemplate> getPengembalianWithPeminjamanById(Long id){
        List<ResponseTemplate> responseList = new ArrayList<>();
        Pengembalian pengembalian = getPengembalianById(id);
        Peminjaman peminjaman = restTemplate.getForObject("http://localhost:8083/api/peminjaman/" + pengembalian.getPeminjaman_id(), Peminjaman.class);
        ResponseTemplate vo = new ResponseTemplate();
        vo.setPeminjaman(peminjaman);
        vo.setPengembalian(pengembalian);
        responseList.add(vo);
        return responseList;
    }

}

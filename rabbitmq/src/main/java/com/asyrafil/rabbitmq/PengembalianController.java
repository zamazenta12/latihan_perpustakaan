package com.asyrafil.rabbitmq;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/pengembalians")
public class PengembalianController {
    private final PengembalianProducerService pengembalianProducerService;
    private final PengembalianRepository pengembalianRepository;

    public PengembalianController(PengembalianProducerService pengembalianProducerService, PengembalianRepository pengembalianRepository) {
        this.pengembalianProducerService = pengembalianProducerService;
        this.pengembalianRepository = pengembalianRepository;
    }

    @PostMapping
    public ResponseEntity<Pengembalian> createPengembalian(@Valid @RequestBody PengembalianRequest pengembalianRequest) {
        Pengembalian pengembalian = new Pengembalian(
                pengembalianRequest.getPeminjamanId(),
                pengembalianRequest.getTanggalPinjam(),
                pengembalianRequest.getTanggalKembali(),
                pengembalianRequest.getDenda(),
                pengembalianRequest.getCustomerEmail());

        Pengembalian savedPengembalian = pengembalianProducerService.createAndSendPengembalian(pengembalian);
        return ResponseEntity.ok(savedPengembalian);
    }

    @GetMapping
    public ResponseEntity<List<Pengembalian>> getAllPengembalians() {
        return ResponseEntity.ok(pengembalianRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pengembalian> getPengembalianById(@PathVariable UUID id) {
        return pengembalianRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pengembalian>> getPengembaliansByStatus(@PathVariable Pengembalian.PengembalianStatus status) {
        return ResponseEntity.ok(pengembalianRepository.findByStatus(status));
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<Pengembalian>> getPengembaliansByCustomer(@PathVariable String email) {
        return ResponseEntity.ok(pengembalianRepository.findByCustomerEmail(email));
    }

}

class PengembalianRequest{
    
    @NotBlank(message = "id peminjaman is required")
    private Integer peminjaman_id;

    @NotBlank(message = "Tanggal Pinjam is required")
    private String tanggal_pinjam;

    @NotBlank(message = "Tanggal Kembali is required")
    private String tanggal_kembali;

    @DecimalMin(value = "0.0", inclusive = false, message = "Denda must be greater than 0")
    private Double denda;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    // Getters and Setters
    public Integer getPeminjamanId() {
        return peminjaman_id;
    }

    public void setPeminjamanId(Integer peminjaman_id) {
        this.peminjaman_id = peminjaman_id;
    }

    public String getTanggalPinjam() {
        return tanggal_pinjam;
    }

    public void setTanggalPinjam(String tanggal_pinjam) {
        this.tanggal_pinjam = tanggal_pinjam;
    }

    public String getTanggalKembali() {
        return tanggal_kembali;
    }

    public void setTanggalKembali(String tanggal_kembali) {
        this.tanggal_kembali = tanggal_kembali;
    }

    public Double getDenda() {
        return denda;
    }

    public void setDenda(Double denda) {
        this.denda = denda;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
}

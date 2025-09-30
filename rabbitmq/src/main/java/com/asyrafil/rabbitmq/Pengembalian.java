package com.asyrafil.rabbitmq;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pengembalians")
public class Pengembalian {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "Peminjaman Id is required")
    @Column(nullable = false)
    private Integer peminjaman_id;

    @NotBlank(message = "Tanggal Pinjam is required")
    @Column(nullable = false)
    private String tanggal_pinjam;

    @NotBlank(message = "Tanggal Kembali is required")
    @Column(nullable = false)
    private String tanggal_kembali;

    private Boolean terlambat;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(nullable = false)
    private Double denda;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Customer email is required")
    @Column(nullable = false)
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PengembalianStatus status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    // Enums
    public enum PengembalianStatus {
        PENDING, PROCESSING, COMPLETED, FAILED
    }

    // Constructors
    public Pengembalian() {
        this.status = PengembalianStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Pengembalian(Integer peminjaman_id, String tanggal_pinjam, String tanggal_kembali,
            Double denda, String customerEmail) {
        this();
        this.peminjaman_id = peminjaman_id;
        this.tanggal_pinjam = tanggal_pinjam;
        this.tanggal_kembali = tanggal_kembali;
        this.denda = denda;
        this.customerEmail = customerEmail;
    }

    // PrePersist callback
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Boolean getTerlambat() {
        return terlambat;
    }

    public void setTerlambat(Boolean terlambat) {
        this.terlambat = terlambat;
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

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public PengembalianStatus getStatus() {
        return status;
    }

    public void setStatus(PengembalianStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public String toString() {
        return "Pengembalian{" +
                "id=" + id +
                ", peminjaman_id='" + peminjaman_id + '\'' +
                ", tanggal_pinjam=" + tanggal_pinjam +
                ", tanggal_kembali=" + tanggal_kembali +
                ", terlambat=" + terlambat +
                ", denda=" + denda +
                ", customerEmail='" + customerEmail + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", processedAt=" + processedAt +
                '}';
    }
}
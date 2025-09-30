package com.asyrafil.rabbitmq;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PengembalianRepository extends JpaRepository<Pengembalian, UUID> {
    List<Pengembalian> findByStatus(Pengembalian.PengembalianStatus status);
    List<Pengembalian> findByCustomerEmail(String customerEmail);
}

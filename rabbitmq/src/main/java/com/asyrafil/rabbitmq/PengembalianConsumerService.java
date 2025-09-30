package com.asyrafil.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PengembalianConsumerService {

    private final PengembalianRepository pengembalianRepository;
    private final EmailService emailService;

    public PengembalianConsumerService(PengembalianRepository pengembalianRepository, EmailService emailService) {
        this.pengembalianRepository = pengembalianRepository;
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void receiveOrder(@Payload Pengembalian pengembalian) {
        try {
            System.out.println("Pengembalian received from RabbitMQ: " + pengembalian);

            // Update status jadi PROCESSING
            pengembalian.setStatus(Pengembalian.PengembalianStatus.PROCESSING);
            pengembalianRepository.save(pengembalian);

            processPengembalian(pengembalian);

            // Update status jadi COMPLETED
            pengembalian.setStatus(Pengembalian.PengembalianStatus.COMPLETED);
            pengembalian.setProcessedAt(java.time.LocalDateTime.now());
            pengembalianRepository.save(pengembalian);

            System.out.println("Pengembalian processed successfully: " + pengembalian.getId());

        } catch (Exception e) {
            System.err.println("Error processing order: " + pengembalian.getId() + ", Error: " + e.getMessage());

            pengembalian.setStatus(Pengembalian.PengembalianStatus.FAILED);
            pengembalianRepository.save(pengembalian);

            throw new RuntimeException("Failed to process order", e);
        }
    }

    private void processPengembalian(Pengembalian pengembalian) {
        System.out.println("Processing pengembalian: " + pengembalian.getId());

        emailService.sendOrderConfirmation(
                pengembalian.getCustomerEmail(),
                pengembalian.getPeminjamanId(),
                pengembalian.getTanggalPinjam(),
                pengembalian.getTanggalKembali(),
                pengembalian.getDenda()
        );

        System.out.println("Order processing completed: " + pengembalian.getId());
    }

}

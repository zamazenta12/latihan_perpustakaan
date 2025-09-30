package com.asyrafil.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PengembalianProducerService {

    private final RabbitTemplate rabbitTemplate;
    private final PengembalianRepository pengembalianRepository;
    
    @Value("${app.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public PengembalianProducerService(RabbitTemplate rabbitTemplate, PengembalianRepository pengembalianRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.pengembalianRepository = pengembalianRepository;
    }

    @Transactional
    public Pengembalian createAndSendPengembalian(Pengembalian pengembalian) {
        // Simpan order ke database
        Pengembalian savedPengembalian = pengembalianRepository.save(pengembalian);
        System.out.println("Pengembalian saved to database: " + savedPengembalian);
        
        // Kirim ke RabbitMQ
        rabbitTemplate.convertAndSend(exchange, routingKey, savedPengembalian);
        System.out.println("Pengembalian sent to RabbitMQ: " + savedPengembalian);
        
        return savedPengembalian;
    }

}

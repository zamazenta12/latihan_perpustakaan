package com.asyrafil.rabbitmq;  

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner{

    private final PengembalianProducerService pengembalianProducerService;

    public DataInitializer(PengembalianProducerService pengembalianProducerService) {
        this.pengembalianProducerService = pengembalianProducerService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Data sample untuk testing
        Pengembalian pengembalian1 = new Pengembalian(1, "12-12-2025", "15-12-2025", 15000000.0, "customer1@example.com");
        Pengembalian pengembalian2 = new Pengembalian(2, "05-05-2025", "10-05-2025",250000.0, "customer2@example.com");
        Pengembalian pengembalian3 = new Pengembalian(3, "01-01-2025", "14-01-2025",500000.0, "customer3@example.com");
        
        pengembalianProducerService.createAndSendPengembalian(pengembalian1);
        pengembalianProducerService.createAndSendPengembalian(pengembalian2);
        pengembalianProducerService.createAndSendPengembalian(pengembalian3);
        
        System.out.println("Sample data initialized");
    }

}

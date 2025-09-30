package com.asyrafil.rabbitmq;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(String to, Integer peminjaman_id, String tanggal_pinjam, String tanggal_kembali, double denda) {
        SimpleMailMessage message = new SimpleMailMessage();
        try {
        message.setTo("rafilrasyid030@gmail.com");
        message.setSubject("Pengembalian Confirmation");
        message.setText("Pengembalian!\n\n" +
                "Peminjaman id: " + peminjaman_id +
                "\nTanggal Pinjam: " + tanggal_pinjam +
                "\nTanggal Kembali: " + tanggal_kembali +
                "\nDenda: Rp" + denda +
                "\n\nWe will process your order shortly.");
        mailSender.send(message);
        }
        catch (Exception e) {
            System.err.println("Gagal mengirim email: " + e.getMessage());
        }
    }

}

package edu.unifor.br.distrischool.studentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String registrationNumber) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Bem-vindo ao DistriSchool!");
            message.setText(String.format(
                    "Olá!\n\n" +
                            "Sua matrícula foi realizada com sucesso!\n" +
                            "Número de matrícula: %s\n\n" +
                            "Acesse o sistema para mais informações.\n\n" +
                            "Atenciosamente,\n" +
                            "Equipe DistriSchool",
                    registrationNumber
            ));

            mailSender.send(message);
            log.info("Email de boas-vindas enviado para: {}", to);
        } catch (Exception e) {
            log.error("Erro ao enviar email de boas-vindas", e);
        }
    }
}
package com.software.modsen.rideservice.service;

import com.software.modsen.rideservice.dto.request.RideFilterRequest;
import com.software.modsen.rideservice.exception.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.software.modsen.rideservice.util.ExceptionMessages.SEND_EMAIL_EXCEPTION;
import static com.software.modsen.rideservice.util.GmailSenderConstants.ATTACHMENT_FILENAME;
import static com.software.modsen.rideservice.util.GmailSenderConstants.FILE_TYPE;
import static com.software.modsen.rideservice.util.GmailSenderConstants.MAIL_SUBJECT;
import static com.software.modsen.rideservice.util.GmailSenderConstants.MAIL_TEXT;
import static com.software.modsen.rideservice.util.GmailSenderConstants.SEND_STATISTICS;

@Service
@RequiredArgsConstructor
@Slf4j
public class GmailSenderService {
    private final JavaMailSender emailSender;
    private final ExcelStatisticsService statisticsService;
    @Value("${spring.mail.username}")
    private String userName;

    public void sendEmail(String receiverEmail, RideFilterRequest filterRequest) {
        byte[] excelData = statisticsService.exportToExcel(filterRequest);
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject(MAIL_SUBJECT);
            helper.setFrom(userName);
            helper.setTo(receiverEmail);
            helper.setText(MAIL_TEXT, true);
            helper.addAttachment(ATTACHMENT_FILENAME, new ByteArrayDataSource(excelData, FILE_TYPE));

            emailSender.send(message);
            log.info(String.format(SEND_STATISTICS, receiverEmail));
        } catch (MessagingException e){
            log.info(e.getMessage());
            throw new EmailSendingException(SEND_EMAIL_EXCEPTION);
        }
    }
}

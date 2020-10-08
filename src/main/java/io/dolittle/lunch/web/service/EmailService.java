// Copyright (c) Dolittle. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package io.dolittle.lunch.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Value("${dolittle.email.order.to}")
    private String sendTo;

    @Value("${dolittle.email.order.subject}")
    private String subject;

    @Value("${dolittle.email.order.from}")
    private String emailFrom;

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final LunchService lunchService;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine, LunchService lunchService) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
        this.lunchService = lunchService;
    }

    public void sendEmail(Map<String, Object> templateModel) throws MessagingException {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = springTemplateEngine.process( "sendOrder.html", thymeleafContext);
        sendHtmlMessage(htmlBody);
    }

    private void sendHtmlMessage(String htmlBody) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(InternetAddress.parse(sendTo));
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        helper.setFrom(emailFrom);

        javaMailSender.send(message);
        lunchService.updateLunchStatus();
        log.info("Lunch order email sent. {}", sendTo);

    }
}

package com.iuni.login.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service("amazonSMTPService")
public class AmazonSMTPService{
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final TemplateEngine htmlTemplateEngine;
    private final String from;

    public AmazonSMTPService(AmazonSimpleEmailService amazonSimpleEmailService, TemplateEngine htmlTemplateEngine, @Value("${aws.ses.from}") String from) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
        this.htmlTemplateEngine = htmlTemplateEngine;
        this.from = from;
    }

    private Context createContext(Map<String, Object> variables){
        Context context = new Context();
        context.setVariables(variables);
        return context;
    }

    private SendEmailRequest createSendEmailRequest(String subject, String content, String... to){
        return new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(to))
                .withSource(from)
                .withMessage(new Message()
                        .withSubject(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(subject))
                        .withBody(new Body().withHtml(new Content().withCharset(StandardCharsets.UTF_8.name()).withData(content)))
                );
    }

    public void send(String subject, Map<String, Object> variables, String... to) {
        String content = htmlTemplateEngine.process("index", createContext(variables));
        SendEmailRequest sendEmailRequest = createSendEmailRequest(subject, content, to);
        this.amazonSimpleEmailService.sendEmail(sendEmailRequest);
    }
}

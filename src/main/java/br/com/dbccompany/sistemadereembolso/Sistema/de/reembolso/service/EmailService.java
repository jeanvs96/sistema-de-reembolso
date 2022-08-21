package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender emailSender;
    private final freemarker.template.Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    public void sendEmail(ReembolsoEntity reembolsoEntity, String sendTo) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(sendTo);

            if (reembolsoEntity.getStatus().equals(StatusReembolso.ABERTO.ordinal())) {
                mimeMessageHelper.setSubject("Olá Gestor! Você tem uma nova solicitação aguardando sua avaliação.");
            } else {
                throw new RegraDeNegocioException("Falha no envio de e-mail");
            }
            mimeMessageHelper.setText(getContentFromTemplate(reembolsoEntity), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (RegraDeNegocioException | MessagingException | IOException | TemplateException | MailException e) {
            log.info("Erro no envio de email");
        }
    }

    public String getContentFromTemplate(ReembolsoEntity reembolsoEntity) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();

        Template template;
        dados.put("titulo", "Título: " + reembolsoEntity.getTitulo());
        dados.put("valor", "Valor = R$ " + reembolsoEntity.getValor());
        dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
        template = fmConfiguration.getTemplate("email-template.html");
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }
}

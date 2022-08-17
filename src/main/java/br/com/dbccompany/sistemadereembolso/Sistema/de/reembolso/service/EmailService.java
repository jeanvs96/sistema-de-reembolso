package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.EmailDto;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import com.fasterxml.jackson.core.JsonProcessingException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
            }
//            else if (reembolsoEntity.getStatus().equals(StatusReembolso.APROVADO_GESTOR.ordinal())) {
//                mimeMessageHelper.setSubject("Olá Financeiro! Você tem uma nova solicitação aguardando sua avaliação.");
//            }
//            else if (reembolsoEntity.getStatus().equals(StatusReembolso.REPROVADO_GESTOR.ordinal())) {
//                mimeMessageHelper.setSubject("Solicitação reprovada.");
//            }
//            else if (reembolsoEntity.getStatus().equals(StatusReembolso.REPROVADO_FINANCEIRO.ordinal())) {
//                mimeMessageHelper.setSubject("Solicitação reprovada.");
//            }
//            else if (reembolsoEntity.getStatus().equals(StatusReembolso.FECHADO_PAGO.ordinal())) {
//                mimeMessageHelper.setSubject("Fechado e encaminhado para pagamento.");
//            }
            else {
                throw new RegraDeNegocioException("Falha no envio de e-mail");
            }
            mimeMessageHelper.setText(getContentFromTemplatePessoa(reembolsoEntity), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (RegraDeNegocioException | MessagingException | IOException | TemplateException e) {
            log.info("Erro no envio de email");
        }
    }

    public String getContentFromTemplatePessoa(ReembolsoEntity reembolsoEntity) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();

        Template template;

        if (reembolsoEntity.getStatus().equals(StatusReembolso.ABERTO.ordinal())) {
            dados.put("intro", "Olá Gestor! \nVocê tem uma nova solicitação aguardando sua avaliação.");
            dados.put("mensagem", "Título: " + reembolsoEntity.getTitulo() +
                    "\nValor = " + reembolsoEntity.getValor());
            dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
            template = fmConfiguration.getTemplate("email-template-copy2.html");

        }else{
            template=null;
        }
//        else if (reembolsoEntity.getStatus().equals(StatusReembolso.APROVADO_GESTOR.ordinal())) {
//            dados.put("nome", "Olá Financeiro! \nVocê tem uma nova solicitação aguardando sua avaliação.");
//            dados.put("mensagem", "Seus dados foram atualizados com sucesso e já podem ser encontrados por empresas e talentos.");
//            dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
//            template = fmConfiguration.getTemplate("email-template-copy2.html");
//
//        }
//        else if (reembolsoEntity.getStatus().equals(StatusReembolso.REPROVADO_GESTOR.ordinal())) {
//            dados.put("nome", "Olá, a sua solicitação foi reprovada!");
//            dados.put("mensagem", "Notamos algumas inconsistencias na sua solicitação, contate o seu gestor para mais detalhes.");
//            dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
//            template = fmConfiguration.getTemplate("email-template-copy2.html");
//
//        }
//        else if (reembolsoEntity.getStatus().equals(StatusReembolso.FECHADO_PAGO.ordinal())) {
//            dados.put("nome", "Olá, sua solicitação foi aprovada pelo financeiro!");
//            dados.put("mensagem", "Sua solicitação foi aprovada e será encaminhada para pagamento.");
//            dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
//            template = fmConfiguration.getTemplate("email-template-copy2.html");
//
//        }
//        else { // REPROVADO_FINANCEIRO
//            dados.put("nome", "Olá, sua solicitação foi reprovada.");
//            dados.put("mensagem", "Sua solicitação foi reprovada pelo financeiro.");
//            dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
//            template = fmConfiguration.getTemplate("email-template-copy2.html");
//        }
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }
}

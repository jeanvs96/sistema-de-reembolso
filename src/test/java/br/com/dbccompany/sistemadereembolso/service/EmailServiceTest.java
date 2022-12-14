package br.com.dbccompany.sistemadereembolso.service;

import br.com.dbccompany.sistemadereembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.enums.StatusReembolso;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private freemarker.template.Configuration fmConfiguration;
    @Mock
    private MimeMessage mimeMessage;

    @Test
    public void deveTestarSendEmailComSucesso() throws IOException {
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        String sendTo = "teste@dbccompany.com.br";
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);

        ReflectionTestUtils.setField(emailService, "from", "jonhytester@dbccompany.com.br");
        doNothing().when(emailSender).send(any(MimeMessage.class));
        when(fmConfiguration.getTemplate(anyString())).thenReturn(new Template("ok", "create", new Configuration()));

        emailService.sendEmail(reembolsoEntity, sendTo);

        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test(expected = WantedButNotInvoked.class)
    public void deveTestarSendEmailSemSucesso() throws IOException, TemplateException {
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        reembolsoEntity.setStatus(StatusReembolso.FECHADO_PAGO.ordinal());

        String sendTo = "teste@dbccompany.com.br";
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(emailService, "from", "somente@dbccompany.com.br");

        emailService.sendEmail(reembolsoEntity, sendTo);

        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }


    private static ReembolsoEntity getReembolsoEntity() {
        ReembolsoEntity reembolsoEntity = new ReembolsoEntity();
        reembolsoEntity.setIdReembolso(1);
        reembolsoEntity.setTitulo("reembolso1");
        reembolsoEntity.setValor(new BigDecimal(100));
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.ordinal());

        return reembolsoEntity;
    }
}

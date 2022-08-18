//package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;
//
//import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ReembolsoRepository;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.test.util.ReflectionTestUtils;
//
//@RunWith(MockitoJUnitRunner.class)
//public class ReembolsoServiceTest {
//    @InjectMocks
//    private ReembolsoService reembolsoService;
//    @Mock
//    private ReembolsoRepository reembolsoRepository;
//    @Mock
//    private UsuarioService usuarioService;
//    @Mock
//    private EmailService emailService;
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ReflectionTestUtils.setField(reembolsoService, "objectMapper", objectMapper);
//    }
//
//
//}

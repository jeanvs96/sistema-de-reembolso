package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioComposeDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ReembolsoRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReembolsoServiceTest {
    @InjectMocks
    private ReembolsoService reembolsoService;
    @Mock
    private ReembolsoRepository reembolsoRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private EmailService emailService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(reembolsoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCreateComSucesso() throws RegraDeNegocioException {
        ReembolsoCreateDTO reembolsoCreateDTO = getReembolsoCreateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoSavedEntity = getReembolsoEntity();
        List<UsuarioComposeDTO> gestores = List.of(getUsuarioComposeDTO());

        when(usuarioService.getLoggedUser()).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoSavedEntity);
        doNothing().when(emailService).sendEmail(any(ReembolsoEntity.class), anyString());
        when(usuarioService.listGestores()).thenReturn(gestores);
        ReembolsoDTO reembolsoSavedDTO = reembolsoService.save(reembolsoCreateDTO);

        assertNotNull(reembolsoSavedDTO);
        assertEquals("Aluguel carro", reembolsoSavedDTO.getTitulo());
    }

    @Test
    public void deveTestarUpdateGestorAprovarComSucesso() throws RegraDeNegocioException {
        Integer idReembolso = 1;
        Boolean aprovado = true;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);

        ReembolsoDTO reembolsoAprovadoDTO = reembolsoService.updateGestorAprovar(idReembolso, aprovado);

        assertNotNull(reembolsoAprovadoDTO);
        assertEquals(StatusReembolso.APROVADO_GESTOR.getTipo(), reembolsoAprovadoDTO.getStatusDoReembolso());
        assertEquals(500L, reembolsoAprovadoDTO.getValor().longValue());
    }
    @Test
    public void deveTestarUpdateGestorAprovarComStatusReprovado_Gestor() throws RegraDeNegocioException {
        Integer idReembolso = 1;
        Boolean aprovado = true;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        reembolsoEntity.setUsuarioEntity(usuarioEntity);
        reembolsoEntity.setStatus(StatusReembolso.REPROVADO_GESTOR.ordinal());

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);

        ReembolsoDTO reembolsoAprovadoDTO = reembolsoService.updateGestorAprovar(idReembolso, aprovado);

        assertNotNull(reembolsoAprovadoDTO);
        assertEquals(StatusReembolso.APROVADO_GESTOR.getTipo(), reembolsoAprovadoDTO.getStatusDoReembolso());
        assertEquals(500L, reembolsoAprovadoDTO.getValor().longValue());
    }

    @Test
    public void deveTestarUpdateGestorReprovarComSucesso() throws RegraDeNegocioException {
        Integer idReembolso = 1;
        Boolean aprovado = false;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        reembolsoEntity.setUsuarioEntity(usuarioEntity);

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        ReembolsoDTO reembolsoReprovadoDTO = reembolsoService.updateGestorAprovar(idReembolso, aprovado);

        assertNotNull(reembolsoReprovadoDTO);
        assertEquals(StatusReembolso.REPROVADO_GESTOR.getTipo(), reembolsoReprovadoDTO.getStatusDoReembolso());
        assertEquals(500L, reembolsoReprovadoDTO.getValor().longValue());
    }

    @Test
    public void deveTestarUpdateFinanceiroPagarComSucesso() throws RegraDeNegocioException {
        Integer idReembolso = 1;
        Boolean aprovado = true;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        reembolsoEntity.setUsuarioEntity(usuarioEntity);

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        ReembolsoDTO reembolsoAprovadoDTO = reembolsoService.updateFinanceiroPagar(idReembolso, aprovado);

        assertNotNull(reembolsoAprovadoDTO);
        assertEquals(StatusReembolso.FECHADO_PAGO.getTipo(), reembolsoAprovadoDTO.getStatusDoReembolso());
        assertEquals(500L, reembolsoAprovadoDTO.getValor().longValue());
    }

    @Test
    public void deveTestarUpdateFinanceiroReprovarComSucesso() throws RegraDeNegocioException {
        Integer idReembolso = 1;
        Boolean aprovado = false;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        reembolsoEntity.setUsuarioEntity(usuarioEntity);

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        ReembolsoDTO reembolsoReprovadoDTO = reembolsoService.updateFinanceiroPagar(idReembolso, aprovado);

        assertNotNull(reembolsoReprovadoDTO);
        assertEquals(StatusReembolso.REPROVADO_FINANCEIRO.getTipo(), reembolsoReprovadoDTO.getStatusDoReembolso());
        assertEquals(500L, reembolsoReprovadoDTO.getValor().longValue());
    }

    @Test
    public void deveTestarListAllByNomeUsuarioComSucesso() {

        List<ReembolsoEntity> reembolsosEntities = List.of(getReembolsoEntity());
        Page<ReembolsoEntity> page = new PageImpl<>(reembolsosEntities);

        when(reembolsoRepository.findAllByLikeNome(anyString(), any(Pageable.class))).thenReturn(page);
        when(reembolsoRepository.findAllByLikeNomeAndStatus(anyString(), anyInt(), any(Pageable.class))).thenReturn(page);

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.listAllByNomeUsuario("teste", StatusReembolso.ABERTO, 0, 10);

        assertNotNull(reembolsoDTOPageDTO);
    }

    @Test
    public void deveTestarListAllReembolsoByStatusComSucesso() {

        List<ReembolsoEntity> reembolsosEntities = List.of(getReembolsoEntity());
        Page<ReembolsoEntity> page = new PageImpl<>(reembolsosEntities);

        when(reembolsoRepository.findAllByStatusOrderByDataEntradaAsc(anyInt(), any(Pageable.class))).thenReturn(page);

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.listAllReembolsosByStatus(StatusReembolso.ABERTO, 0, 10);

        assertNotNull(reembolsoDTOPageDTO);
    }

    @Test
    public void deveTestarListAllReembolsoByStatusTODOSComSucesso() {
        List<ReembolsoEntity> reembolsosEntities = List.of(getReembolsoEntity());
        Page<ReembolsoEntity> page = new PageImpl<>(reembolsosEntities);

        when(reembolsoRepository.findAllOrderByStatusAndDate(any(Pageable.class))).thenReturn(page);

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.listAllReembolsosByStatus(StatusReembolso.TODOS, 0, 10);

        assertNotNull(reembolsoDTOPageDTO);
    }

    @Test
    public void deveTestarUpdateByLoggedUserComSucesso() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        when(usuarioService.getLoggedUser()).thenReturn(usuarioEntity);
        when(reembolsoRepository.findByIdReembolsoAndUsuarioEntity(anyInt(), any(UsuarioEntity.class))).thenReturn(Optional.of(reembolsoEntity));
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);

        ReembolsoDTO reembolsoUpdatedDTO = reembolsoService.updateByLoggedUser(1, getReembolsoCreateDTO());

        assertNotNull(reembolsoUpdatedDTO);

    }

    @Test
    public void deveTestarListAllByLoggedUserAndStatusComSucesso() throws RegraDeNegocioException {
        Page<ReembolsoEntity> page = new PageImpl<>(List.of(getReembolsoEntity()));
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioService.getLoggedUser()).thenReturn(usuarioEntity);
        when(reembolsoRepository.findAllByUsuarioEntityAndStatusOrderByDataEntradaAsc(any(UsuarioEntity.class), anyInt(), any(Pageable.class))).thenReturn(page);

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.listAllByLoggedUserAndStatus(StatusReembolso.ABERTO, 0, 10);

        assertNotNull(reembolsoDTOPageDTO);
    }

    @Test
    public void deveTestarListByIdComSucesso() throws RegraDeNegocioException {
        Integer idReembolso = 1;
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));

        ReembolsoDTO reembolsoDTO = reembolsoService.listById(idReembolso);

        assertNotNull(reembolsoDTO);
    }

    @Test
    public void deveTestarDeleteByLoggedUserComSucesso() throws RegraDeNegocioException {
        Integer idReembolso = 1;
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        Page<ReembolsoEntity> page = new PageImpl<>(List.of(getReembolsoEntity()));
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioService.getLoggedUser()).thenReturn(usuarioEntity);
        when(reembolsoRepository.findAllByUsuarioEntityOrderByStatusAscDataEntradaAsc(any(UsuarioEntity.class), any(Pageable.class))).thenReturn(page);
        when(reembolsoRepository.findByIdReembolsoAndUsuarioEntity(anyInt(), any(UsuarioEntity.class))).thenReturn(Optional.of(reembolsoEntity));
        doNothing().when(reembolsoRepository).delete(any(ReembolsoEntity.class));

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.deleteByLoggedUser(idReembolso, 0, 10);

        verify(reembolsoRepository, times(1)).delete(any(ReembolsoEntity.class));
        assertNotNull(reembolsoDTOPageDTO);
    }

    @Test
    public void deveTestarFindByIdAndUsuarioEntity() throws RegraDeNegocioException {
        Integer idReembolso = 1;
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        when(reembolsoRepository.findByIdReembolsoAndUsuarioEntity(anyInt(), any(UsuarioEntity.class))).thenReturn(Optional.of(reembolsoEntity));

        ReembolsoEntity byIdAndUsuarioEntity = reembolsoService.findByIdAndUsuarioEntity(idReembolso, usuarioEntity);

        assertNotNull(byIdAndUsuarioEntity);
    }


    //    =================== AUXILIARES =========================
    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNome("Testador");
        usuarioEntity.setEmail("testador@dbccompany.com.br");
        usuarioEntity.setSenha("123");
        usuarioEntity.setValorTotal(500.0);

        return usuarioEntity;
    }

    private static ReembolsoEntity getReembolsoEntity() {
        ReembolsoEntity reembolsoEntity = new ReembolsoEntity();
        reembolsoEntity.setIdReembolso(1);
        reembolsoEntity.setTitulo("Aluguel carro");
        reembolsoEntity.setValor(500.0);
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.ordinal());
        return reembolsoEntity;
    }

    private static ReembolsoDTO getReembolsoDTO() {
        ReembolsoDTO reembolsoDTO = new ReembolsoDTO();
        reembolsoDTO.setIdReembolso(1);
        reembolsoDTO.setTitulo("Aluguel carro");
        return reembolsoDTO;
    }

    private static ReembolsoCreateDTO getReembolsoCreateDTO() {
        ReembolsoCreateDTO reembolsoCreateDTO = new ReembolsoCreateDTO();
        reembolsoCreateDTO.setTitulo("Aluguel carro");
        reembolsoCreateDTO.setValor(500.0);
        return reembolsoCreateDTO;
    }

    private static UsuarioComposeDTO getUsuarioComposeDTO() {
        UsuarioComposeDTO usuarioComposeDTO = new UsuarioComposeDTO();
        usuarioComposeDTO.setIdUsuario(1);
        usuarioComposeDTO.setEmail("testador@dbccompany.com.br");
        usuarioComposeDTO.setNome("Testador");
        return usuarioComposeDTO;
    }

}

package br.com.dbccompany.sistemadereembolso.service;

import br.com.dbccompany.sistemadereembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.dto.usuario.UsuarioComposeDTO;
import br.com.dbccompany.sistemadereembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.repository.ReembolsoRepository;
import br.com.dbccompany.sistemadereembolso.repository.UsuarioRepository;
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
import java.math.BigDecimal;
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
    public void deveTestarCreateComSucesso() throws EntidadeNaoEncontradaException {
        ReembolsoCreateDTO reembolsoCreateDTO = getReembolsoCreateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoSavedEntity = getReembolsoEntity();
        List<UsuarioComposeDTO> gestores = List.of(getUsuarioComposeDTO());

        when(usuarioService.getLoggedUser()).thenReturn(usuarioEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoSavedEntity);
        when(usuarioService.listGestores()).thenReturn(gestores);
        ReembolsoDTO reembolsoSavedDTO = reembolsoService.save(reembolsoCreateDTO);

        assertNotNull(reembolsoSavedDTO);
        assertEquals("Aluguel carro", reembolsoSavedDTO.getTitulo());
    }

    @Test
    public void deveTestarUpdateGestorAprovarComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        Integer idReembolso = 1;
        Boolean aprovado = true;
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);

        ReembolsoDTO reembolsoAprovadoDTO = reembolsoService.updateGestorAprovar(idReembolso, aprovado);

        assertNotNull(reembolsoAprovadoDTO);
        assertEquals(StatusReembolso.APROVADO_GESTOR.getTipo(), reembolsoAprovadoDTO.getStatusDoReembolso());
        assertEquals(500L, reembolsoAprovadoDTO.getValor().longValue());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateGestorAprovar() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        Integer idReembolso = 1;
        Boolean aprovado = true;
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        reembolsoEntity.setStatus(2);

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));

        reembolsoService.updateGestorAprovar(idReembolso, aprovado);
    }

    @Test
    public void deveTestarUpdateGestorAprovarComStatusReprovado_Gestor() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
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
    public void deveTestarUpdateGestorReprovarComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
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
    public void deveTestarUpdateFinanceiroPagarComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
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
    public void deveTestarUpdateFinanceiroReprovarComSucesso() throws EntidadeNaoEncontradaException, RegraDeNegocioException {
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

        when(reembolsoRepository.findAllByLikeNomeAndStatus(anyString(), anyInt(), any(Pageable.class))).thenReturn(page);

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.listAllByNomeUsuario("teste", StatusReembolso.ABERTO, 0, 10);

        assertNotNull(reembolsoDTOPageDTO);
    }

    @Test
    public void deveTestarListAllByNomeUsuarioComStatusTodosComSucesso() {
        List<ReembolsoEntity> reembolsosEntities = List.of(getReembolsoEntity());
        Page<ReembolsoEntity> page = new PageImpl<>(reembolsosEntities);

        when(reembolsoRepository.findAllByLikeNome(anyString(), any(Pageable.class))).thenReturn(page);

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.listAllByNomeUsuario("teste", StatusReembolso.TODOS, 0, 10);

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
    public void deveTestarUpdateByIdReembolsoIUsuarioComSucesso() throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        reembolsoEntity.setValor(new BigDecimal(400));
        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);
        when(reembolsoRepository.findByIdReembolsoAndUsuarioEntity(anyInt(), any(UsuarioEntity.class))).thenReturn(Optional.of(reembolsoEntity));
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);

        ReembolsoDTO reembolsoUpdatedDTO = reembolsoService.updateByIdReembolsoIdUsuario(1, usuarioEntity.getIdUsuario(), getReembolsoCreateDTO());

        assertNotNull(reembolsoUpdatedDTO);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateByIdReembolsoIUsuario() throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        reembolsoEntity.setStatus(StatusReembolso.REPROVADO_GESTOR.ordinal());

        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);
        when(reembolsoRepository.findByIdReembolsoAndUsuarioEntity(anyInt(), any(UsuarioEntity.class))).thenReturn(Optional.of(reembolsoEntity));

        ReembolsoDTO reembolsoUpdatedDTO = reembolsoService.updateByIdReembolsoIdUsuario(1, usuarioEntity.getIdUsuario(), getReembolsoCreateDTO());

        assertNotNull(reembolsoUpdatedDTO);

    }

    @Test
    public void deveTestarListAllByLoggedUserAndStatusAbertoComSucesso() throws EntidadeNaoEncontradaException {
        Page<ReembolsoEntity> page = new PageImpl<>(List.of(getReembolsoEntity()));
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioService.getLoggedUser()).thenReturn(usuarioEntity);
        when(reembolsoRepository.findAllByUsuarioEntityAndStatusOrderByDataEntradaAsc(any(UsuarioEntity.class), anyInt(), any(Pageable.class))).thenReturn(page);

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.listAllByLoggedUserAndStatus(StatusReembolso.ABERTO, 0, 10);

        assertNotNull(reembolsoDTOPageDTO);
    }

    @Test
    public void deveTestarListAllByLoggedUserAndStatusTodosComSucesso() throws EntidadeNaoEncontradaException {
        Page<ReembolsoEntity> page = new PageImpl<>(List.of(getReembolsoEntity()));
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioService.getLoggedUser()).thenReturn(usuarioEntity);
        when(reembolsoRepository.findAllByUsuarioEntityOrderByStatusAscDataEntradaAsc(any(UsuarioEntity.class), any(Pageable.class))).thenReturn(page);

        PageDTO<ReembolsoDTO> reembolsoDTOPageDTO = reembolsoService.listAllByLoggedUserAndStatus(StatusReembolso.TODOS, 0, 10);

        assertNotNull(reembolsoDTOPageDTO);
    }

    @Test
    public void deveTestarListByIdComSucesso() throws EntidadeNaoEncontradaException {
        Integer idReembolso = 1;
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();

        when(reembolsoRepository.findById(anyInt())).thenReturn(Optional.of(reembolsoEntity));

        ReembolsoDTO reembolsoDTO = reembolsoService.listById(idReembolso);

        assertNotNull(reembolsoDTO);
    }

    @Test
    public void deveTestarDeleteByIdReembolsoIdUsuarioComSucesso() throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        ReembolsoEntity reembolsoEntity = getReembolsoEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);
        when(reembolsoRepository.findByIdReembolsoAndUsuarioEntity(anyInt(), any(UsuarioEntity.class))).thenReturn(Optional.of(reembolsoEntity));
        doNothing().when(reembolsoRepository).delete(any(ReembolsoEntity.class));

        reembolsoService.deleteByIdReembolsoIdUsuario(reembolsoEntity.getIdReembolso(), usuarioEntity.getIdUsuario());

        verify(reembolsoRepository, times(1)).delete(any(ReembolsoEntity.class));
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

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNome("Testador");
        usuarioEntity.setEmail("testador@dbccompany.com.br");
        usuarioEntity.setSenha("123");
        usuarioEntity.setValorTotal(new BigDecimal(500));

        return usuarioEntity;
    }

    private static ReembolsoEntity getReembolsoEntity() {
        ReembolsoEntity reembolsoEntity = new ReembolsoEntity();
        reembolsoEntity.setIdReembolso(1);
        reembolsoEntity.setTitulo("Aluguel carro");
        reembolsoEntity.setValor(new BigDecimal(500));
        reembolsoEntity.setStatus(StatusReembolso.ABERTO.ordinal());
        return reembolsoEntity;
    }

    private static ReembolsoCreateDTO getReembolsoCreateDTO() {
        ReembolsoCreateDTO reembolsoCreateDTO = new ReembolsoCreateDTO();
        reembolsoCreateDTO.setTitulo("Aluguel carro");
        reembolsoCreateDTO.setValor(new BigDecimal(500));
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

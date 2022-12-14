package br.com.dbccompany.sistemadereembolso.service;

import br.com.dbccompany.sistemadereembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.dto.usuario.*;
import br.com.dbccompany.sistemadereembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.repository.UsuarioRepository;
import br.com.dbccompany.sistemadereembolso.security.TokenService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {
    private ObjectMapper objectMapper = new ObjectMapper();
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolesService rolesService;
    @Mock
    private UsuarioRolesService usuarioRolesService;
    @Mock
    private TokenService tokenService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(usuarioService, "passwordEncoder", passwordEncoder);
    }

    @Test
    public void deveTestarSaveComSucesso() throws RegraDeNegocioException {
        UsuarioCreateDTO usuarioCreateDTO = getUsuarioCreateDTO();
        RolesEntity rolesEntity = getRolesEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setRolesEntities(Set.of(rolesEntity));

        when(rolesService.findByRole(anyString())).thenReturn(rolesEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioLoginComSucessoDTO usuarioLoginComSucessoDTO = usuarioService.save(usuarioCreateDTO);

        assertNotNull(usuarioLoginComSucessoDTO);
        assertEquals(usuarioEntity.getIdUsuario(), usuarioLoginComSucessoDTO.getIdUsuario());
        assertEquals(rolesEntity.getNome(), usuarioLoginComSucessoDTO.getRole());
    }

    @Test
    public void deveTestarSaveByAdminComSucesso() throws RegraDeNegocioException {
        UsuarioCreateDTO usuarioCreateDTO = getUsuarioCreateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        RolesEntity rolesEntity = getRolesEntity();

        when(rolesService.findByRole(anyString())).thenReturn(rolesEntity);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        UsuarioDTO usuarioDTO = usuarioService.saveByAdmin(usuarioCreateDTO, TipoRoles.ADMINISTRADOR);

        assertNotNull(usuarioDTO);
        assertEquals(usuarioEntity.getIdUsuario(), usuarioDTO.getIdUsuario());
        assertEquals(usuarioEntity.getNome(), usuarioDTO.getNome());
        assertEquals(usuarioEntity.getEmail(), usuarioDTO.getEmail());
        assertEquals(usuarioEntity.getValorTotal(), usuarioDTO.getValorTotal());
    }

    @Test
    public void deveTestarDeleteUserComSucesso() throws EntidadeNaoEncontradaException {
        Optional<UsuarioEntity> usuarioEntityOptional = Optional.of(getUsuarioEntity());

        when(usuarioRepository.findById(anyInt())).thenReturn(usuarioEntityOptional);
        doNothing().when(usuarioRepository).delete(any(UsuarioEntity.class));

        usuarioService.deleteUsuario(anyInt());

        verify(usuarioRepository, times(1)).delete(any(UsuarioEntity.class));
    }

    @Test
    public void deveTestarListAllByNome() {
        Pageable pageable = PageRequest.of(0, 1);
        RolesEntity rolesEntity = getRolesEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setRolesEntities(Set.of(rolesEntity));

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), List.of(usuarioEntity).size());
        Page<UsuarioEntity> page = new PageImpl<>(List.of(usuarioEntity).subList(start, end), pageable, List.of(usuarioEntity).size());

        when(usuarioRepository.findAllByNomeContainsIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        PageDTO<UsuarioRolesDTO> usuarioRolesDTOPageDTO = usuarioService.listAllByNome(usuarioEntity.getNome(), 0, 1);

        assertNotNull(usuarioRolesDTOPageDTO);
        assertEquals(Integer.valueOf(0), usuarioRolesDTOPageDTO.getPage());
        assertEquals(Long.valueOf(1), usuarioRolesDTOPageDTO.getTotalElements());
        assertEquals(Integer.valueOf(1), usuarioRolesDTOPageDTO.getTotalPages());
        assertEquals(page.getContent().get(0).getIdUsuario(), usuarioRolesDTOPageDTO.getContent().get(0).getIdUsuario());
    }

    @Test
    public void deveTestarListAll() {
        Pageable pageable = PageRequest.of(0, 1);
        RolesEntity rolesEntity = getRolesEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setRolesEntities(Set.of(rolesEntity));

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), List.of(usuarioEntity).size());
        Page<UsuarioEntity> page = new PageImpl<>(List.of(usuarioEntity).subList(start, end), pageable, List.of(usuarioEntity).size());

        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageDTO<UsuarioRolesDTO> usuarioRolesDTOPageDTO = usuarioService.listAll(0, 1);

        assertNotNull(usuarioRolesDTOPageDTO);
        assertEquals(Integer.valueOf(0), usuarioRolesDTOPageDTO.getPage());
        assertEquals(Long.valueOf(1), usuarioRolesDTOPageDTO.getTotalElements());
        assertEquals(Integer.valueOf(1), usuarioRolesDTOPageDTO.getTotalPages());
        assertEquals(page.getContent().get(0).getIdUsuario(), usuarioRolesDTOPageDTO.getContent().get(0).getIdUsuario());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarSaveComFalhaNoTipoDeEmail() throws RegraDeNegocioException {
        UsuarioCreateDTO usuarioCreateDTO = getUsuarioCreateDTO();
        usuarioCreateDTO.setEmail("teste@gmail.com");

        usuarioService.save(usuarioCreateDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarSaveComFalhaDeEmailExistente() throws RegraDeNegocioException {
        UsuarioCreateDTO usuarioCreateDTO = getUsuarioCreateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuarioEntity));

        usuarioService.save(usuarioCreateDTO);
    }

    @Test
    public void deveTestarAtribuirRoleComSucesso() throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        RolesEntity rolesEntity = getRolesEntity();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setRolesEntities(Set.of(rolesEntity));

        when(rolesService.findByRole(anyString())).thenReturn(rolesEntity);
        doNothing().when(usuarioRolesService).deleteAllByIdUsuario(anyInt());
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));

        UsuarioDTO usuarioDTO = usuarioService.atribuirRole(usuarioEntity.getIdUsuario(), TipoRoles.COLABORADOR);

        assertNotNull(usuarioDTO);
        assertEquals(usuarioDTO.getIdUsuario(), usuarioEntity.getIdUsuario());
        assertEquals(usuarioDTO.getNome(), usuarioEntity.getNome());
        assertEquals(usuarioDTO.getEmail(), usuarioEntity.getEmail());
        assertEquals(usuarioDTO.getValorTotal(), usuarioEntity.getValorTotal());
    }

    @Test
    public void deveTestarFindUsuarioLoggedComSucesso() throws EntidadeNaoEncontradaException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(usuarioEntity.getIdUsuario(), "senha");
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        when(usuarioRepository.findById(any(Integer.class))).thenReturn(Optional.of(usuarioEntity));

        UsuarioDTO usuarioDTO = usuarioService.listUsuarioLogged();

        assertNotNull(usuarioDTO);
        assertEquals(usuarioDTO.getIdUsuario(), usuarioEntity.getIdUsuario());
        assertEquals(usuarioDTO.getEmail(), usuarioEntity.getEmail());
        assertEquals(usuarioDTO.getNome(), usuarioEntity.getNome());
        assertEquals(usuarioDTO.getValorTotal(), usuarioEntity.getValorTotal());
    }

    @Test
    public void deveTestarListarUsuarios() {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        RolesEntity rolesEntity = getRolesEntity();
        rolesEntity.setNome(TipoRoles.GESTOR.getTipo());
        usuarioEntity.setRolesEntities(Set.of(rolesEntity));


        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEntity));

        List<UsuarioComposeDTO> usuarioComposeDTOS = usuarioService.listGestores();

        assertNotNull(usuarioComposeDTOS);
        assertEquals(usuarioComposeDTOS.get(0).getIdUsuario(), usuarioEntity.getIdUsuario());
        assertEquals(usuarioComposeDTOS.get(0).getNome(), usuarioEntity.getNome());
        assertEquals(usuarioComposeDTOS.get(0).getEmail(), usuarioEntity.getEmail());
    }

    private UsuarioCreateDTO getUsuarioCreateDTO() {
        UsuarioCreateDTO usuarioCreateDTO = new UsuarioCreateDTO();
        usuarioCreateDTO.setNome("Jean Silva");
        usuarioCreateDTO.setSenha("123");
        usuarioCreateDTO.setEmail("jean@dbccompany.com.br");

        return usuarioCreateDTO;
    }

    private RolesEntity getRolesEntity() {
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRoles(1);
        rolesEntity.setNome("ROLE_ADMIN");
        return rolesEntity;
    }

    private UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNome("Jean Silva");
        usuarioEntity.setStatus(true);
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setValorTotal(new BigDecimal(0));
        usuarioEntity.setEmail("jean@dbccompany.com.br");
        usuarioEntity.setSenha("123");

        return usuarioEntity;
    }


}

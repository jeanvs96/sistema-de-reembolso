package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginComSucessoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.security.TokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {
    @InjectMocks
    private LoginService loginService;
    @Mock
    private TokenService tokenService;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void deveTestarLoginComSucesso() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO();
        usuarioLoginDTO.setEmail("teste@dbccompany.com.br");
        usuarioLoginDTO.setSenha("123");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(usuarioEntity, usuarioLoginDTO.getSenha());
        usernamePasswordAuthenticationToken.setDetails(usuarioEntity);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        doNothing().when(usuarioService).verificarHostEmail(anyString());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(usernamePasswordAuthenticationToken);

        UsuarioLoginComSucessoDTO usuarioLoginComSucessoDTO = loginService.login(usuarioLoginDTO);

        assertNotNull(usuarioLoginComSucessoDTO);
        assertEquals(usuarioEntity.getIdUsuario(), usuarioLoginComSucessoDTO.getIdUsuario());
        assertEquals(usuarioEntity.getRolesEntities().stream().toList().get(0).getNome(), usuarioLoginComSucessoDTO.getRole());
    }

    private UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNome("Jean Silva");
        usuarioEntity.setStatus(true);
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setValorTotal(new BigDecimal(0));
        usuarioEntity.setEmail("jean@dbccompany.com.br");
        usuarioEntity.setSenha("123");
        usuarioEntity.setRolesEntities(Set.of(getRolesEntity()));

        return usuarioEntity;
    }

    private RolesEntity getRolesEntity() {
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRoles(1);
        rolesEntity.setNome("ROLE_ADMIN");
        return rolesEntity;
    }

}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginComSucessoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    @Value("${jwt.expiration}")
    private String expiration;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    public UsuarioLoginComSucessoDTO login(UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        usuarioLoginDTO.getEmail(),
                        usuarioLoginDTO.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UsuarioEntity usuarioEntity = (UsuarioEntity) authentication.getPrincipal();
        UsuarioLoginComSucessoDTO usuarioLoginComSucessoDTO = new UsuarioLoginComSucessoDTO();
        usuarioLoginComSucessoDTO.setRole(usuarioEntity.getRolesEntities().stream().findFirst().get().getNome());
        usuarioLoginComSucessoDTO.setToken(tokenService.getToken(usuarioEntity, expiration));

        return usuarioLoginComSucessoDTO;
    }
}

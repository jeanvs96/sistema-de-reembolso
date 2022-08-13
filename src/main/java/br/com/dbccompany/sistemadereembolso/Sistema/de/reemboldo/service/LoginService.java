package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto.usuario.UsuarioLoginDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.security.TokenService;
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
    public String login(UsuarioLoginDTO usuarioLoginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        usuarioLoginDTO.getLogin(),
                        usuarioLoginDTO.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return tokenService.getToken((UsuarioEntity) authentication.getPrincipal(), expiration);
    }
}

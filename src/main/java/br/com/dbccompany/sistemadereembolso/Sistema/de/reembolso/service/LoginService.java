package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginComSucessoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
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
    private final UsuarioService usuarioService;
    public UsuarioLoginComSucessoDTO login(UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException {
        usuarioService.verificarHostEmail(usuarioLoginDTO.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        usuarioLoginDTO.getEmail(),
                        usuarioLoginDTO.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UsuarioEntity usuarioEntity = (UsuarioEntity) authentication.getPrincipal();
        RolesEntity rolesEntity = usuarioEntity.getRolesEntities().stream()
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Role não encontrada"));
        UsuarioLoginComSucessoDTO usuarioLoginComSucessoDTO = new UsuarioLoginComSucessoDTO();
        usuarioLoginComSucessoDTO.setRole(rolesEntity.getNome());
        usuarioLoginComSucessoDTO.setToken(tokenService.getToken(usuarioEntity, expiration));
        usuarioLoginComSucessoDTO.setIdUsuario(usuarioEntity.getIdUsuario());

        return usuarioLoginComSucessoDTO;
    }
}

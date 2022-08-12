package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto.usuario.UsuarioLoginDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.repository.UsuarioRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    @Value("${jwt.expiration}")
    private String expiration;
    private final UsuarioRepository usuarioRepository;
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

//    public UsuarioDTO saveUsuario(UsuarioCreateDTO usuarioCreateDTO, TipoPessoa tipoPessoa) throws RegraDeNegocioException {
//        verificarSeEmailExiste(usuarioCreateDTO.getLogin());
//        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);
//        usuarioEntity.setRolesEntities(Set.of(rolesService.findByRole(tipoPessoa.getDescricao())));
//        return entityToDto(usuarioRepository.save(usuarioEntity));
//    }

    public Optional<UsuarioEntity> findByLoginAndSenha(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha);
    }

    public Optional<UsuarioEntity> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}

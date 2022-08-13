package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.security;


import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto.usuario.UsuarioLoginDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UsuarioEntity> optionalUserLogin = usuarioService.findByEmail(email);
        return optionalUserLogin
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Inválido"));

    }
}
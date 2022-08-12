package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.security;


import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UserLoginEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserLoginService userLoginService;

    @Override //2:11
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //buscar usuário pelo login
        Optional<UsuarioEntity> optionalUserLogin = userLoginService.findByLogin(username);

        return (UserDetails) optionalUserLogin
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Inválido"));

    }
}
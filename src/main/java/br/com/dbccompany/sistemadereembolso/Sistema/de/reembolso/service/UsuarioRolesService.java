package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.UsuarioRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioRolesService {
    private final UsuarioRolesRepository usuarioRolesRepository;

    @Transactional
    public void deleteAllByIdUsuario(Integer idUsuario) {
        usuarioRolesRepository.deleteAllByIdUsuario(idUsuario);
    }
}

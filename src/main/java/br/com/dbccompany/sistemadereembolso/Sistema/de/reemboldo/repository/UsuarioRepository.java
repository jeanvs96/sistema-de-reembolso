package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UserLoginEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByEmail(String login);

    Optional<UsuarioEntity> findByEmailAndSenha(String email, String senha);
}

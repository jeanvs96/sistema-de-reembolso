package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByEmailAndSenha(String email, String senha);
}
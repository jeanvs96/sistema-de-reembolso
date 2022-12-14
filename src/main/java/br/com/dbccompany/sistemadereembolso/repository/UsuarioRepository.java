package br.com.dbccompany.sistemadereembolso.repository;

import br.com.dbccompany.sistemadereembolso.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByEmail(String email);

    Page<UsuarioEntity> findAllByNomeContainsIgnoreCase(String nome, Pageable pageable);
}

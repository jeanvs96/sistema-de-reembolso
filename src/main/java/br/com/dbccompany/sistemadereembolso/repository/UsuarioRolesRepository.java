package br.com.dbccompany.sistemadereembolso.repository;

import br.com.dbccompany.sistemadereembolso.entity.UsuarioRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolesRepository extends JpaRepository<UsuarioRolesEntity, Integer> {

    void deleteAllByIdUsuario(Integer idUsuario);
}

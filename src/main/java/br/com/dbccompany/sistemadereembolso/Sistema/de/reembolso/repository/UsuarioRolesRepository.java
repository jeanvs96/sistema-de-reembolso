package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolesRepository extends JpaRepository<UsuarioRolesEntity, Integer> {

    void deleteAllByIdUsuario(Integer idUsuario);
}

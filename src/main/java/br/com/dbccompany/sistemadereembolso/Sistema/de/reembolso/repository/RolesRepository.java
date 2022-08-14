package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface RolesRepository extends JpaRepository<RolesEntity, Integer> {
    Optional<RolesEntity> findRolesEntitiesByNome(String nome);
}

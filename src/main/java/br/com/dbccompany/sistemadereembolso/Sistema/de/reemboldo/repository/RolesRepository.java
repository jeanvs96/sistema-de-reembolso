package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<RolesEntity, Integer> {
    Optional<RolesEntity> findRolesEntitiesByNome(String nome);
}

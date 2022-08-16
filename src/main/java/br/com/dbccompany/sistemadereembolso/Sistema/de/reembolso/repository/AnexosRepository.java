package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.AnexosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnexosRepository extends JpaRepository<AnexosEntity, Integer> {
}

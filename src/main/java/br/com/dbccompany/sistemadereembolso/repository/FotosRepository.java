package br.com.dbccompany.sistemadereembolso.repository;

import br.com.dbccompany.sistemadereembolso.entity.FotosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotosRepository extends JpaRepository<FotosEntity, Integer> {
}

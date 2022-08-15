package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReembolsoRepository extends JpaRepository<ReembolsoEntity, Integer> {

    List<ReembolsoEntity> findAllByUsuarioEntityOrderByStatusAscDataAsc(UsuarioEntity usuarioEntity);

    List<ReembolsoEntity> findAllByOrderByStatusAscDataAsc();
}

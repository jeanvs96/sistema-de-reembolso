package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReembolsoRepository extends JpaRepository<ReembolsoEntity, Integer> {

    Page<ReembolsoEntity> findAllByUsuarioEntityOrderByStatusAscDataAsc(UsuarioEntity usuarioEntity, Pageable pageable);

    Page<ReembolsoEntity> findAllByOrderByStatusAscDataAsc(Pageable pageable);
}

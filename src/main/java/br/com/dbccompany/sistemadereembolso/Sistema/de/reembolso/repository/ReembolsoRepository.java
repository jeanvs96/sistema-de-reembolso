package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReembolsoRepository extends JpaRepository<ReembolsoEntity, Integer> {

    Page<ReembolsoEntity> findAllByUsuarioEntityOrderByStatusAscDataAsc(UsuarioEntity usuarioEntity, Pageable pageable);
    Page<ReembolsoEntity> findAllByUsuarioEntityAndStatusOrderByDataAsc(UsuarioEntity usuarioEntity, Integer status, Pageable pageable);

    Page<ReembolsoEntity> findAllByStatusOrderByDataAsc(Integer status, Pageable pageable);

    Optional<ReembolsoEntity> findByIdReembolsoAndUsuarioEntity(Integer idReembolso, UsuarioEntity usuarioEntity);

    @Query("select r" +
            " from reembolso r" +
            " order by r.status, r.data")
    Page<ReembolsoEntity> findAllOrderByStatusAndDate(Pageable pageable);
}

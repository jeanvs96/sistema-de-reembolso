package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReembolsoRepository extends JpaRepository<ReembolsoEntity, Integer> {

    Page<ReembolsoEntity> findAllByUsuarioEntityOrderByStatusAscDataEntradaAsc(UsuarioEntity usuarioEntity, Pageable pageable);
    Page<ReembolsoEntity> findAllByUsuarioEntityAndStatusOrderByDataEntradaAsc(UsuarioEntity usuarioEntity, Integer status, Pageable pageable);

    Page<ReembolsoEntity> findAllByStatusOrderByDataEntradaAsc(Integer status, Pageable pageable);

    Optional<ReembolsoEntity> findByIdReembolsoAndUsuarioEntity(Integer idReembolso, UsuarioEntity usuarioEntity);

    @Query("select r" +
            " from reembolso r" +
            " order by r.status, r.dataEntrada")
    Page<ReembolsoEntity> findAllOrderByStatusAndDate(Pageable pageable);

    @Query("select r" +
            " from reembolso r" +
            " join r.usuarioEntity u" +
            " where upper(u.nome) like concat('%',upper(:nome),'%') and r.status = :status" +
            " order by u.nome")
    Page<ReembolsoEntity> findAllByLikeNomeAndStatus(@Param("nome") String nome, @Param("status") Integer status, Pageable pageable);

    @Query("select r" +
            " from reembolso r" +
            " join r.usuarioEntity u" +
            " where upper(u.nome) like concat('%',upper(:nome),'%')" +
            " order by u.nome, r.status")
    Page<ReembolsoEntity> findAllByLikeNome(@Param("nome") String nome, Pageable pageable);
}

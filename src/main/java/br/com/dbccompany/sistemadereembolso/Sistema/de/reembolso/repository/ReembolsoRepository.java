package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReembolsoRepository extends JpaRepository<ReembolsoEntity, Integer> {

    List<ReembolsoEntity> findAllByUsuarioEntityAndStatusOrderByDataAsc(UsuarioEntity usuarioEntity, Integer status);

    List<ReembolsoEntity> findAllByStatusOrderByDataAsc(Integer status);

    Optional<ReembolsoEntity> findByIdReembolsoAndUsuarioEntity(Integer idReembolso, UsuarioEntity usuarioEntity);
}

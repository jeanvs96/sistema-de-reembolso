package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity(name = "usuario_roles")
@Getter
@Setter
@RequiredArgsConstructor
public class UsuarioRolesEntity {
    @Id
    @Column(name = "id_usuario_roles")
    private Integer idUsuarioRoles;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_roles")
    private Integer idRoles;
}

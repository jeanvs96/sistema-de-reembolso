package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import java.util.List;

@Entity(name = "roles")
@Getter
@Setter
@RequiredArgsConstructor
public class RolesEntity implements GrantedAuthority {
    @Id
    @SequenceGenerator(name = "roles_seq", sequenceName = "seq_roles", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_seq")
    @Column(name = "id_roles")
    private int idRoles;

    @Column(name = "nome")
    private String nome;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "id_roles"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<UsuarioEntity> usuarioEntities;

    @Override
    public String getAuthority() {
        return this.nome;
    }
}

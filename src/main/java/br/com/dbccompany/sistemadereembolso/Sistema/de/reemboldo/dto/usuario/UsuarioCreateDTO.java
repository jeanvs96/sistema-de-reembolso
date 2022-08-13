package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.enums.TipoRoles;
import lombok.Data;

import java.util.Set;

@Data
public class UsuarioCreateDTO {
    private String nome;
    private String email;
    private String senha;
    private String foto;
    private Set<TipoRoles> roles;
}

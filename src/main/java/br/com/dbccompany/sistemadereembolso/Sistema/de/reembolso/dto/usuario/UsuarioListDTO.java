package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.roles.RolesDTO;
import lombok.Data;

@Data
public class UsuarioListDTO{
    private Integer idUsuario;
    private String nome;
    private String email;
    private RolesDTO rolesDTO;
}

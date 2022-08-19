package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.roles.RolesDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioRolesDTO {
    @Schema(description = "ID exclusivo do usuário.")
    private Integer idUsuario;

    @Schema(description = "Nome do usuário.")
    private String nome;

    @Schema(description = "Email do usuário.")
    private String email;

    @Schema(description = "Objeto com a role do usuário.")
    private RolesDTO rolesDTO;
}

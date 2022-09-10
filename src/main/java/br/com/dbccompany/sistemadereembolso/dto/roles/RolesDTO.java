package br.com.dbccompany.sistemadereembolso.dto.roles;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RolesDTO {
    @Schema(description = "ID exclusivo da role")
    private int idRoles;

    @Schema(description = "Nome da role")
    private String nome;
}

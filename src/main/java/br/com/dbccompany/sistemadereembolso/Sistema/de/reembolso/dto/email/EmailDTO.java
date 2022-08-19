package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.email;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailDTO {
    @Schema(description = "ID exclusivo do usuário.")
    private Integer idUsuario;

    @Schema(description = "Nome do usuário.")
    private String nome;

    @Schema(description = "Email do usuário.")
    private String email;

    @Schema(description = "Foto do reembolso.")
    private String foto;

    @Schema(description = "Status do reembolso.")
    private StatusReembolso status;
}

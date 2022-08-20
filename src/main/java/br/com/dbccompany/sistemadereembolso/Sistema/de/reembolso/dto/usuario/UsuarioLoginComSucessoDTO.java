package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioLoginComSucessoDTO {
    @Schema(description = "ID exclusivo do usuário.")
    Integer idUsuario;

    @Schema(description = "Token válido.")
    String token;

    @Schema(description = "Role do usuário.")
    String role;
}

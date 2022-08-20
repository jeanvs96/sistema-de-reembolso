package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UsuarioComposeDTO {
    @Schema(description = "ID exclusivo do usuário.")
    private Integer idUsuario;

    @Schema(description = "Nome do usuário.")
    private String nome;

    @Schema(description = "")
    private BigDecimal valorTotal;

    @Schema(description = "Email do usuário.")
    private String email;
}

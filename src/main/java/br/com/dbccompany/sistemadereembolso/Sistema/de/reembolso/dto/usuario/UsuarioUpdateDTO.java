package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    @Schema(example = "Seu Nome Completo")
    private String nome;

    @Schema(example = "seuemail@dbccompany.com.br")
    private String email;

    @Schema(example = "Su@Senha!23")
    private String senha;
}

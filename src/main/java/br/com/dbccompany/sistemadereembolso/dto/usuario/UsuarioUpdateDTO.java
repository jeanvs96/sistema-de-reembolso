package br.com.dbccompany.sistemadereembolso.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    @Schema(description = "Nome do usuário.", example = "Seu Nome Completo")
    private String nome;

    @Schema(description = "Email do usuário.", example = "seuemail@dbccompany.com.br")
    private String email;

    @Schema(description = "Senha do usuário", example = "Su@Senha!23")
    private String senha;
}

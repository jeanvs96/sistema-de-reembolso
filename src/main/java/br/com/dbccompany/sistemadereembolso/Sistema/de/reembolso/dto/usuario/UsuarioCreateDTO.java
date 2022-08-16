package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UsuarioCreateDTO {
    @Schema(example = "Seu Nome Completo")
    @NotBlank
    private String nome;

    @Schema(example = "seuemail@dbccompany.com.br")
    @NotNull
    @Email
    private String email;

    @Schema(example = "Sua@Senha!23")
    @NotBlank
    private String senha;
}

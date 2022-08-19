package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UsuarioCreateDTO {
    @Schema(example = "Seu Nome Completo")
    @NotBlank(message = "Insira o nome do usuário")
    private String nome;

    @Schema(example = "seuemail@dbccompany.com.br")
    @NotNull(message = "Insira um email")
    @Email(message = "Insira um email válido")
    private String email;

    @Schema(example = "Sua@Senha!23")
    @NotBlank(message = "Insira uma senha válida")
    @Size(min = 8)
    private String senha;
}

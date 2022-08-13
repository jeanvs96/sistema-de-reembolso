package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UsuarioLoginDTO {

    @Schema(example = "seuemail@dbccompany.com.br")
    @NotNull
    @Email
    private String login;

    @Schema(example = "123")
    @NotNull
    private String senha;
}

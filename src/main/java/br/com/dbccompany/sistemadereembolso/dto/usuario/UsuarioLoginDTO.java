package br.com.dbccompany.sistemadereembolso.dto.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UsuarioLoginDTO {

    @Schema(description = "Email do usuário.", example = "seuemail@dbccompany.com.br")
    @NotNull
    @Email
    private String email;

    @Schema(description = "Senha do usuário", example = "123")
    @NotNull
    private String senha;
}

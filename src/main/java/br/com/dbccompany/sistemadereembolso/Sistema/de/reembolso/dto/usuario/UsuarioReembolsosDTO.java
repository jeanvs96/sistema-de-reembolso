package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioReembolsosDTO {
    @Schema(description = "ID exclusivo do usuário.")
    private Integer idUsuario;

    @Schema(description = "Nome do usuário.")
    private String nome;

    @Schema(description = "Email do usuário.")
    private String email;

    @Schema(description = "Lista de objetos com os reembolsos do usuário.")
    private List<ReembolsoDTO> reembolsosDTO;
}

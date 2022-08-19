package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.FotoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UsuarioDTO {
    @Schema(description = "ID exclusivo do usuário.")
    private Integer idUsuario;

    @Schema(description = "Nome do usuário.")
    private String nome;

    @Schema(description = "Email do usuário.")
    private String email;

    @Schema(description = "Valor da soma dos reembolsos do que o usuário tem para receber.")
    private Double valorTotal;

    @Schema(description = "Objeto com a foto do usuário.")
    private FotoDTO fotoDTO;
}

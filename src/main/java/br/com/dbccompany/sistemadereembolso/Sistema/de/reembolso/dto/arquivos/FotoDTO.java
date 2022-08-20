package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FotoDTO {
    @Schema(description = "ID único da foto.")
    private Integer idFotos;

    @Schema(description = "Nome do arquivo.")
    private String nome;

    @Schema(description = "Tipo do arquivo")
    private String tipo;

    @Schema(description = "Dados (em byte array) do arquivo")
    private byte[] data;
}

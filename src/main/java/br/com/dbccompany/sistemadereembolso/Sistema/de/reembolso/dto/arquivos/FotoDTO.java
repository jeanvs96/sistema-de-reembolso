package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FotoDTO {
    @Schema(name = "ID único da foto.")
    private Integer idFotos;

    @Schema(name = "Nome do arquivo.")
    private String nome;

    @Schema(name = "Tipo do arquivo")
    private String tipo;

    @Schema(name = "Dados (em byte array) do arquivo")
    private byte[] data;
}

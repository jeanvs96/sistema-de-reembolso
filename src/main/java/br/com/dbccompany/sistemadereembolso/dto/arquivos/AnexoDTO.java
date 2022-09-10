package br.com.dbccompany.sistemadereembolso.dto.arquivos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AnexoDTO {
    @Schema(description = "ID único do anexo.")
    private Integer idAnexos;

    @Schema(description = "Nome do arquivo.")
    private String nome;

    @Schema(description = "Tipo do arquivo")
    private String tipo;

    @Schema(description = "Dados (em byte array) do arquivo")
    private byte[] data;
}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AnexoDTO {
    @Schema(name = "ID único do anexo.")
    private Integer idAnexos;

    @Schema(name = "Nome do arquivo.")
    private String nome;

    @Schema(name = "Tipo do arquivo")
    private String tipo;

    @Schema(name = "Dados (em byte array) do arquivo")
    private byte[] data;
}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos;

import lombok.Data;

@Data
public class AnexoDTO {
    private Integer idAnexo;
    private String nome;
    private String tipo;
    private byte[] data;
}

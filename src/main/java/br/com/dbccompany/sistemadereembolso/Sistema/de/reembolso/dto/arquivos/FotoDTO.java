package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos;

import lombok.Data;

@Data
public class FotoDTO {
    private Integer idFotos;
    private String nome;
    private String tipo;
    private byte[] data;
}

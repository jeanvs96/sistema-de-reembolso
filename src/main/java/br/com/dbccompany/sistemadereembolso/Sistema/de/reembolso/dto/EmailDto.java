package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import lombok.Data;

@Data
public class EmailDto {

    private Integer idUsuario;

    private String nome;

    private String email;

    private String foto;

    private StatusReembolso status;
}

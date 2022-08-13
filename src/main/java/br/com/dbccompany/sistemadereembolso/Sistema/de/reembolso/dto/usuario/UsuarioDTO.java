package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String nome;
    private String email;
    private Byte[] foto;
}

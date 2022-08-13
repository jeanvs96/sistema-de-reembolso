package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import lombok.Data;

@Data
public class UsuarioCreateDTO {
    private String nome;
    private String email;
    private String senha;
    private Byte[] foto;
}

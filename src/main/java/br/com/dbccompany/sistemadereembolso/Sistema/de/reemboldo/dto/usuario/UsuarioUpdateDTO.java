package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto.usuario;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    private String nome;
    private String email;
    private String senha;
    private String foto;
}

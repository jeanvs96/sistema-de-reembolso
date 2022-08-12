package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.enums.Cargo;
import lombok.Data;

@Data
public class UsuarioEntity {
    private Integer id;
    private String nome;
    private String senha;
    private String email;
    private String foto;
    private Cargo cargo;
}

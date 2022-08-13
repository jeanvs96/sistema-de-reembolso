package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.enums.TipoRoles;
import lombok.Data;

@Data
public class UsuarioDTO {
    private String nome;
    private String email;
    private String foto;
    private TipoRoles cargo;
}

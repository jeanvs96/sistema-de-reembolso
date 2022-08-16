package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.FotoDTO;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Integer idUsuario;
    private String nome;
    private String email;
    private Double valorTotal;
    private FotoDTO fotoDTO;
}

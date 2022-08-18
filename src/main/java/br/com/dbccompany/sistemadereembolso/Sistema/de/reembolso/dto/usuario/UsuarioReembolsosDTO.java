package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import lombok.Data;

import java.util.List;

@Data
public class UsuarioReembolsosDTO {
    private Integer idUsuario;
    private String nome;
    private String email;
    private List<ReembolsoDTO> reembolsosDTO;
}

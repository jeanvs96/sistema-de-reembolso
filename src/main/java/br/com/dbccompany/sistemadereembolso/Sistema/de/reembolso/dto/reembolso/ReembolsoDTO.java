package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioComposeDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioDTO;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
@Data
public class ReembolsoDTO {
    private Integer idReembolso;
    private LocalDateTime data;
    private Double valor;
    private String status;
    private String titulo;
    private Byte[] anexo;
    private UsuarioComposeDTO usuario;
}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioComposeDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ArquivoEntity;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ReembolsoDTO {
    private Integer idReembolso;
    private LocalDateTime data;
    private Double valor;
    private String statusDoReembolso;
    private String titulo;
    private ArquivoEntity arquivoEntity;
    private UsuarioComposeDTO usuario;
}

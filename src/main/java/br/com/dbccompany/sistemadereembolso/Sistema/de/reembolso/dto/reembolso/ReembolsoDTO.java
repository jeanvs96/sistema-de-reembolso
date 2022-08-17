package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.AnexoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioComposeDTO;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ReembolsoDTO {
    private Integer idReembolso;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataUltimaAlteracao;
    private Double valor;
    private String statusDoReembolso;
    private String titulo;
    private AnexoDTO anexoDTO;
    private UsuarioComposeDTO usuario;
}

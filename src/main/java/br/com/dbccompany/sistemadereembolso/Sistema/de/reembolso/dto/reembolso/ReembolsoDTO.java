package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
@Data
public class ReembolsoDTO {
    private LocalDateTime data;
    private Double valor;
    private String status;
    private String titulo;
    private Byte[] anexo;
    @Column(name = "id_usuario")
    private Integer id_usuario;
}

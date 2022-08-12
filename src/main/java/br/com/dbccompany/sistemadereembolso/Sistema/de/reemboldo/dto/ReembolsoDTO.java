package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.dto;

import java.time.LocalDateTime;

public class ReembolsoDTO {
    private LocalDateTime data;
    private Double valor;
    private String status;
    private String titulo;
    private Integer id_usuario;
}

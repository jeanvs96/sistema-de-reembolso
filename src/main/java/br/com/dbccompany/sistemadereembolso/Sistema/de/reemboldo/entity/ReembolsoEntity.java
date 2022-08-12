package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReembolsoEntity {
    private Integer id;
    private LocalDateTime data;
    private Double valor;
    private String status;
    private String titulo;
    private Integer id_usuario;
}

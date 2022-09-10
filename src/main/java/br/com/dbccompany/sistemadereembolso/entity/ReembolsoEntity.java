package br.com.dbccompany.sistemadereembolso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "reembolso")
@Getter
@Setter
@RequiredArgsConstructor
public class ReembolsoEntity {
    @Id
    @SequenceGenerator(name = "reembolso_seq", sequenceName = "seq_reembolso", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reembolso_seq")
    @Column(name = "id_reembolso")
    private Integer idReembolso;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

    @Column(name = "data_ultima_alteracao")
    private LocalDateTime dataUltimaAlteracao;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "status")
    private Integer status;

    @Column(name = "titulo")
    private String titulo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuarioEntity;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_anexos", referencedColumnName = "id_anexos")
    private AnexosEntity anexosEntity;
}

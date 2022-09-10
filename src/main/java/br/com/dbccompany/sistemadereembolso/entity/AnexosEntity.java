package br.com.dbccompany.sistemadereembolso.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity(name = "anexos")
@Getter
@Setter
@RequiredArgsConstructor
public class AnexosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anexos_seq")
    @SequenceGenerator(name = "anexos_seq", sequenceName = "seq_anexos", allocationSize = 1)
    @Column(name = "id_anexos")
    private Integer idAnexos;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data")
    private byte[] data;

    @OneToOne(mappedBy = "anexosEntity")
    private ReembolsoEntity reembolsoEntity;
}

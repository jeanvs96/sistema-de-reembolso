package br.com.dbccompany.sistemadereembolso.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity(name = "fotos")
@Getter
@Setter
@RequiredArgsConstructor
public class FotosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fotos_seq")
    @SequenceGenerator(name = "fotos_seq", sequenceName = "seq_fotos", allocationSize = 1)
    @Column(name = "id_fotos")
    private Integer idFotos;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data")
    private byte[] data;

    @OneToOne(mappedBy = "fotosEntity")
    private UsuarioEntity usuarioEntity;
}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "arquivos")
@Getter
@Setter
@RequiredArgsConstructor
public class ArquivoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arquivos_seq")
    @SequenceGenerator(name = "arquivos_seq", sequenceName = "seq_arquivos", allocationSize = 1)
    @Column(name = "id_arquivos")
    private Integer idArquivos;

    @Column(name = "nome")
    private String nome;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data")
    @Lob
    private byte[] data;

    @OneToOne(mappedBy = "arquivoEntity")
    private UsuarioEntity usuarioEntity;

    @OneToOne(mappedBy = "arquivoEntity")
    private ReembolsoEntity reembolsoEntity;
}

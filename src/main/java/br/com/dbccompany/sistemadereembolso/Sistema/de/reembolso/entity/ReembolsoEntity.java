package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private LocalDateTime data;

    @Column(name = "valor")
    private Double valor;

    @Column(name = "status")
    private Integer status;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "anexo")
    private Byte[] anexo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuarioEntity;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "anexo_id_arquivos", referencedColumnName = "id_arquivos")
    private ArquivoEntity arquivoEntity;
}

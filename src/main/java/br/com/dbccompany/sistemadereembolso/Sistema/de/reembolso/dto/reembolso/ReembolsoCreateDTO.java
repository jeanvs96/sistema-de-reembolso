package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ReembolsoCreateDTO {
    @Schema(example = "0.0")
    @NotNull
    private Double valor;

    @Schema(example = "Título")
    @NotBlank
    private String titulo;

    @Schema(example = "[]")
    private Byte[] anexo;
}

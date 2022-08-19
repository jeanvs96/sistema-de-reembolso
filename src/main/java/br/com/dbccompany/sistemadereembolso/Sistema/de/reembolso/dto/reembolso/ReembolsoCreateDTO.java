package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ReembolsoCreateDTO {
    @Schema(example = "0.0")
    @NotNull(message = "Insira o valor do reembolso")
    private Double valor;

    @Schema(example = "Transporte para reunião com cliente")
    @NotBlank(message = "Insira uma descrição paro o reembolso")
    private String titulo;

}

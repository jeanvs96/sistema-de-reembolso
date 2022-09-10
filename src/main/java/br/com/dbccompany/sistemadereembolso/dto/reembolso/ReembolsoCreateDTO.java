package br.com.dbccompany.sistemadereembolso.dto.reembolso;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ReembolsoCreateDTO {
    @Schema(description = "Valor do reembolso",example = "0.0")
    @NotNull(message = "Insira o valor do reembolso")
    private BigDecimal valor;

    @Schema(description = "Título com a descrição do reembolso", example = "Transporte para reunião com cliente")
    @NotBlank(message = "Insira uma descrição paro o reembolso")
    private String titulo;

}

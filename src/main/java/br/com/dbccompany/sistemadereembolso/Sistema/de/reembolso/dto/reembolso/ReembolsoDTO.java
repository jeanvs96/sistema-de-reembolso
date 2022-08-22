package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.arquivos.AnexoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioComposeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReembolsoDTO {
    @Schema(description = "ID único do reembolso.")
    private Integer idReembolso;

    @Schema(description = "Data de criação do reembolso")
    private LocalDateTime dataEntrada;

    @Schema(description = "Data da última alteração de status do reembolso")
    private LocalDateTime dataUltimaAlteracao;

    @Schema(description = "Valor do reembolso")
    private BigDecimal valor;

    @Schema(description = "Status do reembolso")
    private String statusDoReembolso;

    @Schema(description = "Título descrevendo o reembolso")
    private String titulo;

    @Schema(description = "Objeto com o anexo do reembolso")
    private AnexoDTO anexoDTO;

    @Schema(description = "Objeto com o usuário à quem o reembolso pertence")
    private UsuarioComposeDTO usuario;
}

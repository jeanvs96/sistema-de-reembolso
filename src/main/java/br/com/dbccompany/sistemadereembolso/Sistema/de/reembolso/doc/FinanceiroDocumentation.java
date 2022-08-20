package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface FinanceiroDocumentation {
    @PutMapping("/pagar/{idReembolso}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status do reembolso alterado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Altera status do reembolso.", description = "Altera status do reembolso para pago(true) ou reprovado pelo financeiro(falso).")
    ResponseEntity<ReembolsoDTO> updatePagar(@PathVariable("idReembolso") Integer idReembolso, @RequestParam Boolean pagar) throws EntidadeNaoEncontradaException, RegraDeNegocioException;
}

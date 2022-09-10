package br.com.dbccompany.sistemadereembolso.controller;

import br.com.dbccompany.sistemadereembolso.doc.FinanceiroDocumentation;
import br.com.dbccompany.sistemadereembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.service.ReembolsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/financeiro")
@RequiredArgsConstructor
@Validated
public class FinanceiroController implements FinanceiroDocumentation {
    private final ReembolsoService reembolsoService;

    @PutMapping("/pagar/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updatePagar(@PathVariable("idReembolso") Integer idReembolso, @RequestParam Boolean pagar) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateFinanceiroPagar(idReembolso, pagar), HttpStatus.OK);
    }
}

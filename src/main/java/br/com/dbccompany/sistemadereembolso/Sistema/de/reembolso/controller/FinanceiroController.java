package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc.FinanceiroDocumentation;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ReembolsoService;
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
    public ResponseEntity<ReembolsoDTO> updatePagar(@PathVariable("idReembolso") Integer idReembolso, @RequestParam Boolean pagar) throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(reembolsoService.updateFinanceiroPagar(idReembolso, pagar), HttpStatus.OK);
    }
}

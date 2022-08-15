package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ReembolsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/financeiro")
@RequiredArgsConstructor
@Validated
public class FinanceiroController {
    private final ReembolsoService reembolsoService;


    @GetMapping("/listAdmin") //    TODO - fazer listagem reembolsos somente com status "aprovado_gestor"
    public ResponseEntity<List<ReembolsoDTO>> listAdmin() {
        return new ResponseEntity<>(reembolsoService.findAll(), HttpStatus.OK);
    }

    @PutMapping("/pagar/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updatePagar(@PathVariable("idReembolso") Integer idReembolso, @RequestParam Boolean pagar) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateFinanceiroPagar(idReembolso, pagar), HttpStatus.OK);
    }
}

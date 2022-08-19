package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ReembolsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gestor")
@RequiredArgsConstructor
@Validated
public class GestorController {
    private final ReembolsoService reembolsoService;

    @PutMapping("/aprovar/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updateAprovar(@PathVariable("idReembolso") Integer idReembolso, @RequestParam Boolean aprovado) throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(reembolsoService.updateGestorAprovar(idReembolso, aprovado), HttpStatus.OK);
    }
}

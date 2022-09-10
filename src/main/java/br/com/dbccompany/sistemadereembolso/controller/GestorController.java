package br.com.dbccompany.sistemadereembolso.controller;

import br.com.dbccompany.sistemadereembolso.doc.GestorDocumentation;
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
@RequestMapping("/gestor")
@RequiredArgsConstructor
@Validated
public class GestorController implements GestorDocumentation {
    private final ReembolsoService reembolsoService;

    @PutMapping("/aprovar/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updateAprovar(@PathVariable("idReembolso") Integer idReembolso, @RequestParam Boolean aprovado) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateGestorAprovar(idReembolso, aprovado), HttpStatus.OK);
    }
}

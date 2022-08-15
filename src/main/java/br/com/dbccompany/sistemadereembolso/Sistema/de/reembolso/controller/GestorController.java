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
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
@Validated
public class GestorController {
    private final ReembolsoService reembolsoService;


//    @GetMapping("/listAdmin") //    TODO - fazer listagem reembolsos somente com status "aberto"
//    public ResponseEntity<List<ReembolsoDTO>> listAdmin() {
//        return new ResponseEntity<>(reembolsoService.findAll(), HttpStatus.OK);
//    }

    @PutMapping("/aprovar/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updateAprovar(@PathVariable("idReembolso") Integer idReembolso, @RequestParam Boolean aprovado) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateGestorAprovar(idReembolso, aprovado), HttpStatus.OK);
    }
}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ReembolsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reembolso")
@RequiredArgsConstructor
@Validated
public class ReembolsoController {
    private final ReembolsoService reembolsoService;

    @PostMapping("/create")
    public ResponseEntity<ReembolsoDTO> create(@RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.create(reembolsoCreateDTO), HttpStatus.OK);
    }

    @GetMapping("/list/status")
    public ResponseEntity<PageDTO<ReembolsoDTO>> listAllByStatus(@RequestParam StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros){
        return new ResponseEntity<>(reembolsoService.listAllReembolsosByStatus(statusReembolso, pagina, quantidadeDeRegistros), HttpStatus.OK);
    }

    @GetMapping("/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> listById(@PathVariable("idReembolso") Integer idReembolso) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.listById(idReembolso), HttpStatus.OK);
    }

    @GetMapping("/logged/list/status")
    public ResponseEntity<PageDTO<ReembolsoDTO>> listAllByLoggedUserAndStatus(@RequestParam StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.listAllByLoggedUserAndStatus(statusReembolso, pagina, quantidadeDeRegistros), HttpStatus.OK);
    }

    @PutMapping("/logged/update/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updateByLoggedUser(@PathVariable("idReembolso") Integer idReembolso ,@RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateByLoggedUser(idReembolso, reembolsoCreateDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/logged/delete/{idReembolso}")
    public ResponseEntity<PageDTO<ReembolsoDTO>> deleteByLoggedUser(@PathVariable("idReembolso") Integer idReembolso, Integer pagina, Integer quantidadeDeRegistros) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.deleteByLoggedUser(idReembolso, pagina, quantidadeDeRegistros), HttpStatus.OK);
    }
}

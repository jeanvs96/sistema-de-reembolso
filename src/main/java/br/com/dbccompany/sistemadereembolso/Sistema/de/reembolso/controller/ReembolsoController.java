package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc.ReembolsoDocumentation;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ReembolsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/reembolso")
@RequiredArgsConstructor
@Validated
public class ReembolsoController  implements ReembolsoDocumentation {
    private final ReembolsoService reembolsoService;

    @PostMapping("/create")
    public ResponseEntity<ReembolsoDTO> create(@Valid @RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(reembolsoService.save(reembolsoCreateDTO), HttpStatus.OK);
    }

    @PutMapping("/update/{idReembolso}/usuario/{idUsuario}")
    public ResponseEntity<ReembolsoDTO> updateByLoggedUser(@PathVariable("idReembolso") Integer idReembolso, @PathVariable("idUsuario") Integer idUsuario, @Valid @RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        return new ResponseEntity<>(reembolsoService.updateByIdReembolsoIdUsuario(idReembolso, idUsuario,reembolsoCreateDTO), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{idReembolso}/usuario/{idUsuario}")
    public ResponseEntity.BodyBuilder deleteByLoggedUser(@PathVariable("idReembolso") Integer idReembolso, @PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        reembolsoService.deleteByIdReembolsoIdUsuario(idReembolso, idUsuario);
        return ResponseEntity.ok();
    }

    @GetMapping("/list/status")
    public ResponseEntity<PageDTO<ReembolsoDTO>> listAllByStatus(@RequestParam StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros){
        return new ResponseEntity<>(reembolsoService.listAllReembolsosByStatus(statusReembolso, pagina, quantidadeDeRegistros), HttpStatus.OK);
    }

    @GetMapping("/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> listById(@PathVariable("idReembolso") Integer idReembolso) throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(reembolsoService.listById(idReembolso), HttpStatus.OK);
    }

    @GetMapping("/logged/list/status")
    public ResponseEntity<PageDTO<ReembolsoDTO>> listAllByLoggedUserAndStatus(@RequestParam StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros) throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(reembolsoService.listAllByLoggedUserAndStatus(statusReembolso, pagina, quantidadeDeRegistros), HttpStatus.OK);
    }

    @GetMapping("/list/nome/status")
    public ResponseEntity<PageDTO<ReembolsoDTO>> listByNome(@RequestParam("nome") String nome, StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros){
        return new ResponseEntity<>(reembolsoService.listAllByNomeUsuario(nome, statusReembolso, pagina, quantidadeDeRegistros), HttpStatus.OK);
    }
}

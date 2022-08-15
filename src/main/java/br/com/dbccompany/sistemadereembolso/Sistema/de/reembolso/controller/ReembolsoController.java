package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
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
    @GetMapping("/list")
    public ResponseEntity<List<ReembolsoDTO>> listAll(){
        return new ResponseEntity<>(reembolsoService.findAll(), HttpStatus.OK);
    }
    @PutMapping("/updateAdmin/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updateAdmin(@PathVariable("idReembolso") Integer idReembolso ,@RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateAdmin(idReembolso, reembolsoCreateDTO), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/deleteAdmin/{idReembolso}")
    public void deleteAdmin(@PathVariable("idReembolso") Integer idReembolso){
        reembolsoService.deleteAdmin(idReembolso);
    }
    //    =================== METODOS DO PROPRIO USUARIO LOGADO ========================
    @GetMapping("/logged")
    public ResponseEntity<List<ReembolsoDTO>> findAllByLoggedUser() throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.listByLoggedUser(), HttpStatus.OK);
    }

    @PutMapping("/updateProprio/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updateProprio(@PathVariable("idReembolso") Integer idReembolso ,@RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateByLoggedUser(idReembolso, reembolsoCreateDTO), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/deleteProprio/{idReembolso}")
    public void deleteProprio(@PathVariable("idReembolso") Integer idReembolso) throws RegraDeNegocioException {
        reembolsoService.deleteProprio(idReembolso);
    }
}

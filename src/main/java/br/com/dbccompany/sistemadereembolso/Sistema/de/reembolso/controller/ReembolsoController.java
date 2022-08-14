package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.roles.RolesDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ReembolsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/solicitacoes")
@RequiredArgsConstructor
@Validated
public class ReembolsoController {
    private final ReembolsoService reembolsoService;

    @PostMapping("/create")
    public ResponseEntity<ReembolsoDTO> createSolicitacao(@RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.create(reembolsoCreateDTO), HttpStatus.OK);
    }
    @GetMapping("/listAdmin")
    public ResponseEntity<List<ReembolsoDTO>> listAdmin(){
        return new ResponseEntity<>(reembolsoService.listAdmin(), HttpStatus.OK);
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
    @GetMapping("/listProprio")
    public ResponseEntity<Set<ReembolsoEntity>> listProprio() throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.listProprio(), HttpStatus.OK);
    }

    @PutMapping("/updateProprio/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updateProprio(@PathVariable("idReembolso") Integer idReembolso ,@RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateProprio(idReembolso, reembolsoCreateDTO), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/deleteProprio/{idReembolso}")
    public void deleteProprio(@PathVariable("idReembolso") Integer idReembolso) throws RegraDeNegocioException {
        reembolsoService.deleteProprio(idReembolso);
    }
}

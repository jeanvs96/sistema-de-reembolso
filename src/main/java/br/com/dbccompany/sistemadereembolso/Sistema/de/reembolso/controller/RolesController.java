package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.roles.RolesDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO - permitir acesso somente ao ADMIN
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Validated
public class RolesController {
    private final RolesService rolesService;

    @PostMapping("/insert")
    public ResponseEntity<RolesDTO> insertRole(@RequestParam TipoRoles tipoRoles) {
        return new ResponseEntity<>(rolesService.insertRole(tipoRoles), HttpStatus.OK);
    }
    @GetMapping("/list")
    public ResponseEntity<List<RolesDTO>> findAll(){
        return new ResponseEntity<>(rolesService.findAll(), HttpStatus.OK);
    }
    @PutMapping("/update/{idRole}")
    public ResponseEntity<RolesDTO> updateRole(@PathVariable("idRole") Integer idRole ,@RequestBody TipoRoles tipoRoles){
        return new ResponseEntity<>(rolesService.updateRoleById(idRole, tipoRoles), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/remove/{idRole}")
    public void removeRole(@PathVariable("idRole") TipoRoles tipoRoles){
        rolesService.removeRole(tipoRoles);
    }

}

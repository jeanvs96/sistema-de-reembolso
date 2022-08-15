package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioUpdateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.AtivarDesativarUsuario;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.LoginService;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {
    private final UsuarioService usuarioService;
    private final LoginService loginService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.saveUsuario(usuarioCreateDTO), HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.update(usuarioUpdateDTO), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{idUsuario}")
    public void deletarUsuario(@PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException {
        usuarioService.deleteUsuario(idUsuario);
    }
    @PutMapping("/ativar-desativar-usuario/{idUsuario}")
    public ResponseEntity<String> ativarDesativarUsuario(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam AtivarDesativarUsuario ativarDesativarUsuario) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.ativarDesativarUsuario(idUsuario, ativarDesativarUsuario), HttpStatus.OK);
    }

    @PostMapping("/role")
    public ResponseEntity<UsuarioDTO> atribuirRole(Integer idUsuario, TipoRoles role) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.atribuirRole(idUsuario, role), HttpStatus.OK);
    }
}

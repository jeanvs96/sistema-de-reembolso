package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc.UsuarioDocumentation;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginDTO;
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
import java.util.Set;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Validated
public class UsuarioController implements UsuarioDocumentation {
    private final UsuarioService usuarioService;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
        return new ResponseEntity<>(loginService.login(usuarioLoginDTO), HttpStatus.OK);
    }

    @PostMapping("/cadastro-usuario")
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO, @RequestParam Set<TipoRoles> roles) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.saveUsuario(usuarioCreateDTO, roles), HttpStatus.OK);
    }

    @GetMapping("/logged")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
    }

    @PutMapping("/ativar-desativar-usuario/{idUsuario}")
    public ResponseEntity<String> ativarDesativarUsuario(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam AtivarDesativarUsuario ativarDesativarUsuario) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.ativarDesativarUsuario(idUsuario, ativarDesativarUsuario), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.update(usuarioUpdateDTO), HttpStatus.OK);
    }
}

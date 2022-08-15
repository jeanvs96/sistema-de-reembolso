package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginDTO;
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
@RequestMapping("/login")
@RequiredArgsConstructor
@Validated
public class LoginController {
    private final UsuarioService usuarioService;
    private final LoginService loginService;

    @GetMapping("/logged")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(loginService.login(usuarioLoginDTO), HttpStatus.OK);
    }
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.saveUsuario(usuarioCreateDTO), HttpStatus.OK);
    }
}

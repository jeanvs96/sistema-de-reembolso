package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc.UsuarioDocumentation;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.*;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
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
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Validated
public class UsuarioController implements UsuarioDocumentation {
    private final UsuarioService usuarioService;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<UsuarioLoginComSucessoDTO> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(loginService.login(usuarioLoginDTO), HttpStatus.OK);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioLoginComSucessoDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.save(usuarioCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idUsuario}")
    public void deletarUsuario(@PathVariable("idUsuario") Integer idUsuario) throws EntidadeNaoEncontradaException {
        usuarioService.deleteUsuario(idUsuario);
    }

    @GetMapping("/logged")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws EntidadeNaoEncontradaException {
        return new ResponseEntity<>(usuarioService.listUsuarioLogged(), HttpStatus.OK);
    }

    @GetMapping("/listar/nome")
    public PageDTO<UsuarioRolesDTO> listByNome(String nome, Integer pagina, Integer quantidadeDeRegistros) {
        return usuarioService.listAllByNome(nome, pagina, quantidadeDeRegistros);
    }

    @GetMapping("/listar")
    public PageDTO<UsuarioRolesDTO> list(Integer pagina, Integer quantidadeDeRegistros) {
        return usuarioService.listAll(pagina, quantidadeDeRegistros);
    }
}

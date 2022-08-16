package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc.UsuarioDocumentation;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.*;
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
import java.util.List;

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

    @GetMapping("/logged")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.findUsuarioLogged(), HttpStatus.OK);
    }

    @GetMapping("/listar")
    public List<UsuarioRelatorioDTO> listarRelatorioUsuarios(){
        return usuarioService.listarUsuarios();
    }

//    @PutMapping("/ativar-desativar-usuario/{idUsuario}")
//    public ResponseEntity<String> ativarDesativarUsuario(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam AtivarDesativarUsuario ativarDesativarUsuario) throws RegraDeNegocioException {
//        return new ResponseEntity<>(usuarioService.ativarDesativarUsuario(idUsuario, ativarDesativarUsuario), HttpStatus.OK);
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException {
//        return new ResponseEntity<>(usuarioService.update(usuarioUpdateDTO), HttpStatus.OK);
//    }

//    @DeleteMapping("/delete/{idUsuario}")
//    public void deletarUsuario(@PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException {
//        usuarioService.deleteUsuario(idUsuario);
//    }
}

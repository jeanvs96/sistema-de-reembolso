package br.com.dbccompany.sistemadereembolso.controller;

import br.com.dbccompany.sistemadereembolso.doc.AdminDocumentation;
import br.com.dbccompany.sistemadereembolso.dto.usuario.UsuarioCreateDTO;
import br.com.dbccompany.sistemadereembolso.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController implements AdminDocumentation {
    private final UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO, TipoRoles role) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.saveByAdmin(usuarioCreateDTO, role), HttpStatus.OK);
    }

    @PostMapping("/atribuir/role")
    public ResponseEntity<UsuarioDTO> atribuirRole(Integer idUsuario, TipoRoles role) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        return new ResponseEntity<>(usuarioService.atribuirRole(idUsuario, role), HttpStatus.OK);
    }
}

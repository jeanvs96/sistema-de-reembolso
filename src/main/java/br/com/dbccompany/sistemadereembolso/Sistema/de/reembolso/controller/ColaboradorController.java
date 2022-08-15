package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioUpdateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.LoginService;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ReembolsoService;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/colaborador")
@RequiredArgsConstructor
@Validated
public class ColaboradorController {
    private final UsuarioService usuarioService;
    private final ReembolsoService reembolsoService;

    @PutMapping("/update") // TODO - criar update do seus proprios dados (/update)
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.update(usuarioUpdateDTO), HttpStatus.OK);
    }

    @PostMapping("/reembolso/solicitar")
    public ResponseEntity<ReembolsoDTO> createSolicitacao(@RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.create(reembolsoCreateDTO), HttpStatus.OK);
    }

    @GetMapping("/reembolso/listar")      // TODO - fazer relatorio paginado
    public ResponseEntity<Set<ReembolsoEntity>> listProprio() throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.listProprio(), HttpStatus.OK);
    }

    @PutMapping("/reembolso/update/{idReembolso}")
    public ResponseEntity<ReembolsoDTO> updateProprio(@PathVariable("idReembolso") Integer idReembolso , @RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(reembolsoService.updateProprio(idReembolso, reembolsoCreateDTO), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/reembolso/delete/{idReembolso}")
    public void deleteProprio(@PathVariable("idReembolso") Integer idReembolso) throws RegraDeNegocioException {
        reembolsoService.deleteProprio(idReembolso);
    }
}

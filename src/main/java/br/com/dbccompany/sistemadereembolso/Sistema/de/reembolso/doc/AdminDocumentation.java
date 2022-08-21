package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

public interface AdminDocumentation {
    @PostMapping("/cadastro")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário Inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Criar um usuário.", description = "Cria um usuário, podendo atribuir um cargo.")
    ResponseEntity<UsuarioDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO, TipoRoles role) throws RegraDeNegocioException;

    @PostMapping("/atribuir/role")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Cargo atribuído com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário Inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Atribuir um cargo a um usuário.", description = "Atribui um cargo a um usuário buscando este pelo seu id.")
    ResponseEntity<UsuarioDTO> atribuirRole(Integer idUsuario, TipoRoles role) throws RegraDeNegocioException, EntidadeNaoEncontradaException;
}

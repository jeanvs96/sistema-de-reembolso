package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioLoginDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.UsuarioUpdateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.AtivarDesativarUsuario;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Set;

public interface UsuarioDocumentation {

    @PostMapping("/login")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Login inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Autenticar usuário.", description = "Valida através da geração de um token a existência do par login/senha cadastrado no banco de dados.")
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO);

    @PostMapping("/cadastro-usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Cadastro criado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao criar cadastro.")
            }
    )
    @Operation(summary = "Cadastrar usuário.", description = "Cadastra um usuário no banco de dados, com uma ou mais roles(ADMIN, GESTOR, FINANCEIRO, COLABORADOR).")
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException;

    @PostMapping("/role")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Role atribuidad com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao criar cadastro.")
            }
    )
    @Operation(summary = "Atualiza role do usuário.", description = "Altera a role (ADMIN, GESTOR, FINANCEIRO, COLABORADOR) do usuário pelo ID, caso seja passada a role ADMIN, todas as roles serão atribuídas.")
    public ResponseEntity<UsuarioDTO> atribuirRole(Integer idUsuario, TipoRoles role) throws RegraDeNegocioException;
    @GetMapping("/logged")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o usuário logado."),
                    @ApiResponse(responseCode = "404", description = "O usuário não foi encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao buscar entidade logada.")
            }
    )
    @Operation(summary = "Recuperar usuario logado.", description = "Recupera do banco o usuario logado.")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException;

    @PutMapping("/ativar-desativar-usuario/{idUsuario}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status do usuário alterado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado,"),
                    @ApiResponse(responseCode = "500", description = "Falha ao desativar/ativar usuário.")
            }
    )
    @Operation(summary = "Ativar/Desativar usuario.", description = "Muda o status do usuário no banco de dados para Ativado ou Desativado.")
    public ResponseEntity<String> ativarDesativarUsuario(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam AtivarDesativarUsuario ativarDesativarUsuario) throws RegraDeNegocioException;

    @PutMapping("/update")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Não foi possível encontrar este usuário."),
                    @ApiResponse(responseCode = "500", description = "Falha ao atualizar esses dados.")
            }
    )
    @Operation(summary = "Atualizar usuário.", description = "Atualiza o usuário logado no banco de dados, podendo alterar um ou vários dados do usuário.")
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException;
}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario.*;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

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
    ResponseEntity<UsuarioLoginComSucessoDTO> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) throws RegraDeNegocioException;

    @PostMapping("/cadastro")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Cadastro criado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao criar cadastro.")
            }
    )
    @Operation(summary = "Cadastrar usuário.", description = "Cadastra um usuário no banco de dados, com uma ou mais roles(ADMIN, GESTOR, FINANCEIRO, COLABORADOR).")
    ResponseEntity<UsuarioLoginComSucessoDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException;

    @DeleteMapping("/delete/{idUsuario}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao criar cadastro.")
            }
    )
    @Operation(summary = "Deletar usuário.", description = "Endpoint destinado à testes automatizados, pode ser acessado somente pela ROLE_ADMIN")
    void deletarUsuario(@PathVariable("idUsuario") Integer idUsuario) throws EntidadeNaoEncontradaException;

    @GetMapping("/logged")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna o usuário logado."),
                    @ApiResponse(responseCode = "404", description = "O usuário não foi encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao buscar entidade logada.")
            }
    )
    @Operation(summary = "Recuperar usuario logado.", description = "Recupera do banco o usuario logado.")
    ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @GetMapping("/listar/nome")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma lista de usuários"),
                    @ApiResponse(responseCode = "404", description = "O usuário não foi encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao buscar entidade logada.")
            }
    )
    @Operation(summary = "Recuperar lista de usuários.", description = "Recupera do banco todos os usuários que atendam o filtro por nome, ou parte dele.")
    PageDTO<UsuarioRolesDTO> listByNome(String nome, Integer pagina, Integer quantidadeDeRegistros);

    @GetMapping("/listar")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna uma lista de usuários"),
                    @ApiResponse(responseCode = "404", description = "O usuário não foi encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao buscar entidade logada.")
            }
    )
    @Operation(summary = "Recuperar lista de usuários.", description = "Recupera do banco todos os usuários.")
    PageDTO<UsuarioRolesDTO> list(Integer pagina, Integer quantidadeDeRegistros);
}

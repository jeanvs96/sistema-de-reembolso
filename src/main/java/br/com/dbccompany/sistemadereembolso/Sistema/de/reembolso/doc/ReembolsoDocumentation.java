package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.paginacao.PageDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoCreateDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.reembolso.ReembolsoDTO;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.StatusReembolso;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface ReembolsoDocumentation {
    @PostMapping("/create")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Solicitação de reembolso criada com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Reembolso Inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Criar uma solicitação de reembolso.", description = "Cria a solicitação de um reembolso, e encaminha para aprovação do gestor, informando-o através de e-mail.")
    public ResponseEntity<ReembolsoDTO> create(@Valid @RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws EntidadeNaoEncontradaException;

    @PutMapping("/update/{idReembolso}/usuario/{idUsuario}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Reembolso atualizado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Reembolso inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Atualizar reembolso do usuario logado.", description = "Atualiza um reembolso específico do usuario logado, buscando pelo id do reembolso.")
    public ResponseEntity<ReembolsoDTO> updateByLoggedUser(@PathVariable("idReembolso") Integer idReembolso, @PathVariable("idUsuario") Integer idUsuario, @Valid @RequestBody ReembolsoCreateDTO reembolsoCreateDTO) throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @DeleteMapping("/delete/{idReembolso}/usuario/{idUsuario}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Reembolso deletado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Reembolso inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Deletar reembolso do usuario logado.", description = "Deleta um reembolso específico do usuario logado, buscando pelo id do reembolso.")
    public ResponseEntity.BodyBuilder deleteByLoggedUser(@PathVariable("idReembolso") Integer idReembolso, @PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException, EntidadeNaoEncontradaException;

    @GetMapping("/list/status")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Reembolsos exibidos com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Reembolso inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Listar paginando os reembolsos pelo status.", description = "Lista todos os reembolsos, filtrando pelo seu status e entregando com paginação.")
    public ResponseEntity<PageDTO<ReembolsoDTO>> listAllByStatus(@RequestParam StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros);

    @GetMapping("/{idReembolso}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Reembolso exibido com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Reembolso inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Buscar um reembolso pelo seu id.", description = "Exibe um reembolso buscando pelo seu id.")
    public ResponseEntity<ReembolsoDTO> listById(@PathVariable("idReembolso") Integer idReembolso) throws EntidadeNaoEncontradaException;

    @GetMapping("/logged/list/status")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Reembolsos exibidos com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Reembolso inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Listar paginando os reembolsos pelo usuario logado e status.", description = "Lista todos os reembolsos do usuario logado, filtrando pelo seu status e entregando com paginação.")
    public ResponseEntity<PageDTO<ReembolsoDTO>> listAllByLoggedUserAndStatus(@RequestParam StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros) throws EntidadeNaoEncontradaException;

    @GetMapping("/list/nome/status")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Reembolsos exibidos com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Reembolso inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Listar paginando os reembolsos pelo nome.", description = "Lista todos os reembolsos, filtrando pelo seu nome e entregando com paginação.")
    public ResponseEntity<PageDTO<ReembolsoDTO>> listByNome(@RequestParam("nome") String nome, StatusReembolso statusReembolso, Integer pagina, Integer quantidadeDeRegistros);
    }

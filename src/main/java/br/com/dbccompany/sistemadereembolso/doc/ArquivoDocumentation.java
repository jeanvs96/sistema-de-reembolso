package br.com.dbccompany.sistemadereembolso.doc;

import br.com.dbccompany.sistemadereembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface ArquivoDocumentation {
    @PostMapping(value = "/foto", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Upload de foto realizado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Upload de foto do usuário.", description = "Faz upload de foto do usuário, criando relação entre as duas entidades no banco de dados.")
    ResponseEntity<String> uploadFoto(@RequestPart("file") MultipartFile file) throws EntidadeNaoEncontradaException, RegraDeNegocioException;


    @PostMapping(value = "/anexo/reembolso/usuario", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Upload de anexo realizado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro no servidor.")
            }
    )
    @Operation(summary = "Upload de anexo do reembolso.", description = "Faz upload de upload de anexo do reembolso, criando relação entre as duas entidades no banco de dados.")
    ResponseEntity<String> uploadAnexo(@RequestPart("file") MultipartFile file, Integer idReembolso, Integer idUsuario) throws  RegraDeNegocioException, EntidadeNaoEncontradaException;
}

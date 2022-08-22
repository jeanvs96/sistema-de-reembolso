package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.doc.ArquivoDocumentation;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ArquivosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ArquivoController implements ArquivoDocumentation {
    private final ArquivosService arquivosService;

    @PostMapping(value = "/foto", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFoto(@RequestPart("file") MultipartFile file) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        return new ResponseEntity<>(arquivosService.saveFoto(file), HttpStatus.OK);
    }

    @PostMapping(value = "/anexo/reembolso/usuario", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadAnexo(@RequestPart("file") MultipartFile file, Integer idReembolso, Integer idUsuario) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        return new ResponseEntity<>(arquivosService.saveAnexo(file, idReembolso, idUsuario), HttpStatus.OK);
    }
}

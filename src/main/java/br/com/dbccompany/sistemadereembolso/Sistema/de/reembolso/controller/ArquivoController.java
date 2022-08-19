package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ArquivosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivosService arquivosService;

    @PostMapping(value = "/foto", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFoto(@RequestPart("file") MultipartFile file) throws EntidadeNaoEncontradaException, IOException, RegraDeNegocioException {
        return new ResponseEntity<>(arquivosService.saveFoto(file), HttpStatus.OK);
    }

    @PostMapping(value = "/anexo", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadAnexo(@RequestPart("file") MultipartFile file, Integer idReembolso) throws IOException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        return new ResponseEntity<>(arquivosService.saveAnexo(file, idReembolso), HttpStatus.OK);
    }
}

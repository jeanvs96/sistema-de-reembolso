package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.controller;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ArquivoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service.ArquivosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivosService arquivosService;

    @PostMapping("/foto")
    public ResponseEntity<ArquivoEntity> uploadFoto(@RequestParam("file") MultipartFile file) throws IOException, RegraDeNegocioException {
        return new ResponseEntity<>(arquivosService.saveFoto(file), HttpStatus.OK);
    }

    @PostMapping("/anexo")
    public ResponseEntity<ArquivoEntity> uploadAnexo(@RequestParam("file") MultipartFile file, Integer idReembolso) throws IOException, RegraDeNegocioException {
        return new ResponseEntity<>(arquivosService.saveAnexo(file, idReembolso), HttpStatus.OK);
    }
}

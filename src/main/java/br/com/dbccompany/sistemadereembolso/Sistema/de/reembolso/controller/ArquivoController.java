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
@RequestMapping("/arquivo")
@RequiredArgsConstructor
public class ArquivoController {

    private final ArquivosService arquivosService;

    @PostMapping( "/upload")
    public ResponseEntity<ArquivoEntity> uploadFile(MultipartFile file) throws IOException, RegraDeNegocioException {
        return new ResponseEntity<>(arquivosService.saveFoto(file), HttpStatus.OK);
    }
}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FormDataEntity {
    private MultipartFile image;
}

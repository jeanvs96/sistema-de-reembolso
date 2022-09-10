package br.com.dbccompany.sistemadereembolso.component;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Component
public class MultipartFileDataReader {

    public byte[] readData(MultipartFile file) throws IOException {
        return file.getBytes();
    }
}

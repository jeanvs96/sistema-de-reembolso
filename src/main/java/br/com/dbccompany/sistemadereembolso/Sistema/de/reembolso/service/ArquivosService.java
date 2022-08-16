package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ArquivoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ArquivosRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ArquivosService {
    private final ArquivosRepository arquivosRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public ArquivoEntity saveFoto(MultipartFile file) throws IOException, RegraDeNegocioException {
        ArquivoEntity arquivoEntity = new ArquivoEntity();
        arquivoEntity.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
        arquivoEntity.setTipo(file.getContentType());
        arquivoEntity.setData(file.getBytes());

        ArquivoEntity arquivoEntitySalvo = arquivosRepository.save(arquivoEntity);

        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        usuarioEntity.setArquivoEntity(arquivoEntitySalvo);
        usuarioRepository.save(usuarioEntity);

        return arquivoEntitySalvo;
    }


}


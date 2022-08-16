package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ArquivoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ArquivosRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.ReembolsoRepository;
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
    private final ReembolsoService reembolsoService;
    private final ReembolsoRepository reembolsoRepository;

    public ArquivoEntity saveFoto(MultipartFile file) throws IOException, RegraDeNegocioException {
        ArquivoEntity arquivoEntitySalvo = arquivosRepository.save(createArquivoEntity(file));

        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        usuarioEntity.setArquivoEntity(arquivoEntitySalvo);
        usuarioRepository.save(usuarioEntity);

        return arquivoEntitySalvo;
    }


    public ArquivoEntity saveAnexo(MultipartFile file, Integer idReembolso) throws IOException, RegraDeNegocioException {
        ArquivoEntity arquivoEntitySalvo = arquivosRepository.save(createArquivoEntity(file));

        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        ReembolsoEntity reembolsoEntity = reembolsoService.findByIdAndUsuarioEntity(idReembolso, usuarioEntity);
        reembolsoEntity.setArquivoEntity(arquivoEntitySalvo);

        reembolsoRepository.save(reembolsoEntity);

        return arquivoEntitySalvo;
    }

    public ArquivoEntity createArquivoEntity(MultipartFile file) throws IOException {
        ArquivoEntity arquivoEntity = new ArquivoEntity();
        arquivoEntity.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
        arquivoEntity.setTipo(file.getContentType());
        arquivoEntity.setData(file.getBytes());

        return arquivoEntity;
    }
}


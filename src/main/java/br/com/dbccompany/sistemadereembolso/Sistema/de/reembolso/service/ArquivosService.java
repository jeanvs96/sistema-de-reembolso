package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.AnexosEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.FotosEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.AnexosRepository;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.FotosRepository;
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
    private final FotosRepository fotosRepository;
    private final AnexosRepository anexosRepository;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final ReembolsoService reembolsoService;
    private final ReembolsoRepository reembolsoRepository;

    public FotosEntity saveFoto(MultipartFile file) throws IOException, RegraDeNegocioException {
        FotosEntity fotosEntity = new FotosEntity();
        fotosEntity.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
        fotosEntity.setTipo(file.getContentType());
        fotosEntity.setData(file.getBytes());

        FotosEntity arquivoEntitySalvo = fotosRepository.save(fotosEntity);

        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        usuarioEntity.setFotosEntity(arquivoEntitySalvo);
        usuarioRepository.save(usuarioEntity);

        return arquivoEntitySalvo;
    }


    public AnexosEntity saveAnexo(MultipartFile file, Integer idReembolso) throws IOException, RegraDeNegocioException {
        AnexosEntity anexosEntity = new AnexosEntity();
        anexosEntity.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
        anexosEntity.setTipo(file.getContentType());
        anexosEntity.setData(file.getBytes());

        AnexosEntity anexosEntitySalvo = anexosRepository.save(anexosEntity);

        UsuarioEntity usuarioEntity = usuarioService.getLoggedUser();
        ReembolsoEntity reembolsoEntity = reembolsoService.findByIdAndUsuarioEntity(idReembolso, usuarioEntity);
        reembolsoEntity.setAnexosEntity(anexosEntitySalvo);

        reembolsoRepository.save(reembolsoEntity);

        return anexosEntitySalvo;
    }


}


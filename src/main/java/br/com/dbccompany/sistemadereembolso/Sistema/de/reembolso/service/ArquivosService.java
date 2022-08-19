package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.AnexosEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.FotosEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.EntidadeNaoEncontradaException;
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

    public String saveFoto(MultipartFile file) throws EntidadeNaoEncontradaException, IOException {
        try {
            FotosEntity fotosEntity = new FotosEntity();
            fotosEntity.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
            fotosEntity.setTipo(file.getContentType());
            fotosEntity.setData(file.getBytes());
            UsuarioEntity usuarioEntityLogado = usuarioService.getLoggedUser();
            FotosEntity fotosEntityRecuperada = usuarioEntityLogado.getFotosEntity();

            if (!(fotosEntityRecuperada == null)) {
                fotosEntity.setIdFotos(usuarioEntityLogado.getFotosEntity().getIdFotos());
            }

            FotosEntity arquivoEntitySalvo = fotosRepository.save(fotosEntity);

            usuarioEntityLogado.setFotosEntity(arquivoEntitySalvo);
            usuarioRepository.save(usuarioEntityLogado);
        } catch (IOException e) {
            throw new IOException("Falha ao fazer upload do arquivo");
        }
        return "Foto salva com sucesso";
    }


    public String saveAnexo(MultipartFile file, Integer idReembolso) throws IOException, RegraDeNegocioException, EntidadeNaoEncontradaException {
        try {
            AnexosEntity anexosEntity = new AnexosEntity();
            anexosEntity.setNome(StringUtils.cleanPath(file.getOriginalFilename()));
            anexosEntity.setTipo(file.getContentType());
            anexosEntity.setData(file.getBytes());
            UsuarioEntity usuarioEntityRecuperado = usuarioService.getLoggedUser();
            ReembolsoEntity reembolsoEntityRecuperado = reembolsoService.findByIdAndUsuarioEntity(idReembolso, usuarioEntityRecuperado);
            AnexosEntity anexosEntityRecuperado = reembolsoEntityRecuperado.getAnexosEntity();

            if (!(anexosEntityRecuperado == null)) {
                anexosEntity.setIdAnexos(anexosEntityRecuperado.getIdAnexos());
            }

            AnexosEntity anexosEntitySalvo = anexosRepository.save(anexosEntity);

            reembolsoEntityRecuperado.setAnexosEntity(anexosEntitySalvo);

            reembolsoRepository.save(reembolsoEntityRecuperado);
        } catch (IOException e) {
            throw new RegraDeNegocioException("Falha ao fazer upload do arquivo");
        }

        return "Arquivo salvo com sucesso";
    }


}


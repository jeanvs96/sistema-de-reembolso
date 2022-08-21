package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.component.MultipartFileDataReader;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.AnexosEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.FotosEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.UsuarioEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoArquivoAnexo;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoArquivoFoto;
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
    private final MultipartFileDataReader multipartFileDataReader;

    public String saveFoto(MultipartFile file) throws EntidadeNaoEncontradaException, RegraDeNegocioException {
        try {
            verificarTipoDeFoto(file);

            FotosEntity fotosEntity = new FotosEntity();
            fotosEntity.setNome(file.getOriginalFilename());
            fotosEntity.setTipo(file.getContentType());
            fotosEntity.setData(multipartFileDataReader.readData(file));
            UsuarioEntity usuarioEntityLogado = usuarioService.getLoggedUser();
            FotosEntity fotosEntityRecuperada = usuarioEntityLogado.getFotosEntity();

            if (!(fotosEntityRecuperada == null)) {
                fotosEntity.setIdFotos(usuarioEntityLogado.getFotosEntity().getIdFotos());
            }

            FotosEntity arquivoEntitySalvo = fotosRepository.save(fotosEntity);

            usuarioEntityLogado.setFotosEntity(arquivoEntitySalvo);
            usuarioRepository.save(usuarioEntityLogado);
        } catch (IOException e) {
            throw new RegraDeNegocioException("Falha ao fazer upload do arquivo");
        }
        return "Foto salva com sucesso";
    }


    public String saveAnexo(MultipartFile file, Integer idReembolso, Integer idUsuario) throws RegraDeNegocioException, EntidadeNaoEncontradaException {
        try {
            verificarTipoDeAnexo(file);

            AnexosEntity anexosEntity = new AnexosEntity();
            anexosEntity.setNome(file.getOriginalFilename());
            anexosEntity.setTipo(file.getContentType());
            anexosEntity.setData(multipartFileDataReader.readData(file));
            UsuarioEntity usuarioEntityRecuperado = usuarioService.findById(idUsuario);
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

    private void verificarTipoDeFoto(MultipartFile file) throws RegraDeNegocioException {
        boolean controle = false;
        for (TipoArquivoFoto tipoArquivoFoto : TipoArquivoFoto.values()) {
            if (tipoArquivoFoto.getTipo().equals(file.getContentType())) {
                controle = true;
            }
        }
        if (!controle) {
            throw new RegraDeNegocioException("Selecione uma foto PNG/JPG/JPEG");
        }
    }

    private void verificarTipoDeAnexo(MultipartFile file) throws RegraDeNegocioException {
        boolean controle = false;
        for (TipoArquivoAnexo tipoArquivoAnexo : TipoArquivoAnexo.values()) {
            if (tipoArquivoAnexo.getTipo().equals(file.getContentType())) {
                controle = true;
            }
        }
        if (!controle) {
            throw new RegraDeNegocioException("Selecione um anexo PNG/JPG/JPEG/PDF");
        }
    }
}


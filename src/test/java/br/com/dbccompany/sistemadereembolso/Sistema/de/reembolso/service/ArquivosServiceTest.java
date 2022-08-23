package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.component.MultipartFileDataReader;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArquivosServiceTest {
    @InjectMocks
    private ArquivosService arquivosService;
    @Mock
    private FotosRepository fotosRepository;
    @Mock
    private AnexosRepository anexosRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ReembolsoService reembolsoService;
    @Mock
    private ReembolsoRepository reembolsoRepository;
    @Mock
    private MultipartFileDataReader multipartFileDataReader;


    @Test
    public void deveTestarSaveFotoComSucesso() throws EntidadeNaoEncontradaException, IOException, RegraDeNegocioException {

        MockMultipartFile file = new MockMultipartFile("file", "orig", "image/png", "bar".getBytes());

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioService.getLoggedUser()).thenReturn(usuarioEntity);

        FotosEntity foto = getFotosEntity();
        foto.setNome("Foto Teste");
        foto.setData(file.getBytes());
        when(fotosRepository.save(any(FotosEntity.class))).thenReturn(foto);

        usuarioEntity.setFotosEntity(foto);
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        String stringSucesso = arquivosService.saveFoto(file);

        assertNotNull(stringSucesso);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarSaveFoto() throws RegraDeNegocioException, IOException, EntidadeNaoEncontradaException {
        MockMultipartFile file = new MockMultipartFile("file", "orig", "image/png", "bar".getBytes());
        when(multipartFileDataReader.readData(any(MultipartFile.class))).thenThrow(IOException.class);

        arquivosService.saveFoto(file);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarSaveFotoErroAoVerificarTipoDeFoto() throws RegraDeNegocioException, IOException, EntidadeNaoEncontradaException {
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        arquivosService.saveFoto(file);
    }

    @Test
    public void deveTestarSaveAnexoComSucesso() throws RegraDeNegocioException, IOException, EntidadeNaoEncontradaException {
        MockMultipartFile file = new MockMultipartFile("file", "orig", "application/pdf", "bar".getBytes());

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioService.findById(anyInt())).thenReturn(usuarioEntity);

        ReembolsoEntity reembolsoEntity = new ReembolsoEntity();
        reembolsoEntity.setIdReembolso(1);
        reembolsoEntity.setUsuarioEntity(usuarioEntity);
        when(reembolsoService.findByIdAndUsuarioEntity(anyInt(), any(UsuarioEntity.class))).thenReturn(reembolsoEntity);

        AnexosEntity anexosEntity = new AnexosEntity();
        anexosEntity.setIdAnexos(1);
        anexosEntity.setNome("Anexo1");
        anexosEntity.setData(file.getInputStream().readAllBytes());
        anexosEntity.setReembolsoEntity(reembolsoEntity);

        reembolsoEntity.setAnexosEntity(anexosEntity);
        when(anexosRepository.save(any(AnexosEntity.class))).thenReturn(anexosEntity);
        when(reembolsoRepository.save(any(ReembolsoEntity.class))).thenReturn(reembolsoEntity);

        String stringSucesso = arquivosService.saveAnexo(file, 1, 1);

        assertNotNull(stringSucesso);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarSaveAnexo() throws RegraDeNegocioException, IOException, EntidadeNaoEncontradaException {
        MockMultipartFile file = new MockMultipartFile("file", "orig", "application/pdf", "bar".getBytes());

        when(multipartFileDataReader.readData(any(MultipartFile.class))).thenThrow(IOException.class);

        arquivosService.saveAnexo(file, 1, 1);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarSaveAnexoErroAoVerificarTipoDeAnexo() throws RegraDeNegocioException, IOException, EntidadeNaoEncontradaException {
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        arquivosService.saveAnexo(file, anyInt(), anyInt());
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.setNome("Teste Unitario");
        usuarioEntity.setStatus(true);
        usuarioEntity.setEmail("teste@dbccompany.com.br");
        usuarioEntity.setSenha("123");

        return usuarioEntity;
    }

    private static FotosEntity getFotosEntity() {
        FotosEntity fotosEntity = new FotosEntity();
        fotosEntity.setIdFotos(1);
        fotosEntity.setNome("self");
        fotosEntity.setUsuarioEntity(getUsuarioEntity());
        return fotosEntity;
    }
}

package br.com.dbccompany.sistemadereembolso.service;

import br.com.dbccompany.sistemadereembolso.repository.UsuarioRolesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioRolesServiceTest {

    @InjectMocks
    private UsuarioRolesService usuarioRolesService;
    @Mock
    private UsuarioRolesRepository usuarioRolesRepository;

    @Test
    public void deveTestarDeleteAllByIdUsuarioComSucesso() {
        doNothing().when(usuarioRolesRepository).deleteAllByIdUsuario(anyInt());

        usuarioRolesService.deleteAllByIdUsuario(anyInt());

        verify(usuarioRolesRepository, times(1)).deleteAllByIdUsuario(anyInt());
    }


}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums.TipoRoles;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.RolesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.internet.MimeMessage;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RolesServiceTest {
    @InjectMocks
    private RolesService rolesService;
    @Mock
    private RolesRepository rolesRepository;

    @Test
    public void deveTestarFindByRoleComSucesso() throws RegraDeNegocioException {
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRoles(1);
        rolesEntity.setNome(TipoRoles.COLABORADOR.getTipo());

        when(rolesRepository.findRolesEntitiesByNome(anyString())).thenReturn(Optional.of(rolesEntity));

        RolesEntity role = rolesService.findByRole(TipoRoles.GESTOR.getTipo());

        assertNotNull(role);
        assertEquals(1,role.getIdRoles());
    }

    @Test
    public void deveTestarFindByRoleComException() throws RegraDeNegocioException {
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRoles(1);
        rolesEntity.setNome(TipoRoles.COLABORADOR.getTipo());

        when(rolesRepository.findRolesEntitiesByNome(anyString())).thenReturn(Optional.of(rolesEntity));

        RolesEntity role = rolesService.findByRole("cozinheiro");

        verify(rolesRepository, times(1)).findRolesEntitiesByNome(anyString());

    }
}

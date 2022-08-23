package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.service;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.exceptions.RegraDeNegocioException;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.repository.RolesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RolesServiceTest {

    @InjectMocks
    private RolesService rolesService;
    @Mock
    private RolesRepository rolesRepository;

    @Test
    public void deveTestarFindByRoleComSucesso() throws RegraDeNegocioException {
        RolesEntity rolesEntity = getRolesEntity();

        when(rolesRepository.findRolesEntitiesByNome(anyString())).thenReturn(Optional.of(rolesEntity));

        RolesEntity rolesEntityReturn = rolesService.findByRole(anyString());

        assertNotNull(rolesEntityReturn);
        assertEquals(rolesEntity.getIdRoles(), rolesEntityReturn.getIdRoles());
        assertEquals(rolesEntity.getNome(), rolesEntityReturn.getNome());
    }

    private RolesEntity getRolesEntity() {
        RolesEntity rolesEntity = new RolesEntity();
        rolesEntity.setIdRoles(1);
        rolesEntity.setNome("ROLE_ADMIN");
        return rolesEntity;
    }
}

package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.ReembolsoEntity;
import br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.entity.RolesEntity;
import lombok.Data;

import java.util.Set;
@Data
public class UsuarioRelatorioDTO extends UsuarioDTO{
    private Set<RolesEntity> rolesEntities;
    private Set<ReembolsoEntity> reembolsoEntities;

}

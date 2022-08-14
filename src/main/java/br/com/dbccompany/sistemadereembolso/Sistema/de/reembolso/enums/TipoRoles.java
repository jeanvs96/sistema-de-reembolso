package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums;

import java.util.Arrays;

public enum TipoRoles {
    COLABORADOR("ROLE_COLABORADOR"),
    GESTOR("ROLE_GESTOR"),
    FINANCEIRO("ROLE_FINANCEIRO"),
    ADMINISTRADOR("ROLE_ADMIN");

    private final String tipoRoles;

    TipoRoles(String cargo) {
        this.tipoRoles = cargo;
    }

    public String getTipo() {
        return tipoRoles;
    }

    public static TipoRoles ofTipo(String tipoRoles) {
        return Arrays.stream(TipoRoles.values())
                .filter(tp -> tp.getTipo().equals(tipoRoles))
                .findFirst()
                .get();
    }
}

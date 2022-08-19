package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums;

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
}

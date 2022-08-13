package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums;

import java.util.Arrays;

public enum TipoRoles {
    COLABORADOR("ROLE_COLABORADOR"),
    GESTOR("ROLE_GESTOR"),
    FINANCEIRO("ROLE_FINANCEIRO"),
    ADMINISTRADOR("ROLE_ADMIN");

    private String tipoMensagem;

    TipoRoles(String tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public String getTipo() {
        return tipoMensagem;
    }

    public static TipoRoles ofTipo(String tipoMensagem) {
        return Arrays.stream(TipoRoles.values())
                .filter(tp -> tp.getTipo().equals(tipoMensagem))
                .findFirst()
                .get();
    }
}

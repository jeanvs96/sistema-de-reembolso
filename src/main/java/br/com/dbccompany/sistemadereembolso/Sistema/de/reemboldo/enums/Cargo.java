package br.com.dbccompany.sistemadereembolso.Sistema.de.reemboldo.enums;

import java.util.Arrays;

public enum Cargo {
    ROLE_COLABORADOR("colaborador"),
    ROLE_GESTOR("gestor"),
    ROLE_FINANCEIRO("financeiro"),
    ROLE_ADMINISTRADOR("colaborador");

    private String tipoMensagem;

    Cargo(String tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public String getTipo() {
        return tipoMensagem;
    }

    public static Cargo ofTipo(String tipoMensagem) {
        return Arrays.stream(Cargo.values())
                .filter(tp -> tp.getTipo().equals(tipoMensagem))
                .findFirst()
                .get();
    }
}

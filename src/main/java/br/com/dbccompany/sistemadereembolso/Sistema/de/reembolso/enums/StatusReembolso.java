package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums;

import java.util.Arrays;

public enum StatusReembolso {
    ABERTO("aberto"),
    APROVADO_GESTOR("aprovado gestor"),
    REPROVADO_GESTOR("reprovado gestor"),
    REPROVADO_FINANCEIRO("reprovado financeiro"),
    FECHADO_PAGO("fechado(pago)")
    ;
    private final String statusReembolso;

    StatusReembolso(String situacao) {
        this.statusReembolso = situacao;
    }

    public String getTipo() {
        return statusReembolso;
    }

    public static StatusReembolso ofTipo(String statusReembolso) {
        return Arrays.stream(StatusReembolso.values())
                .filter(tp -> tp.getTipo().equals(statusReembolso))
                .findFirst()
                .get();
    }
}

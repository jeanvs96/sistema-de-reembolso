package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums;

public enum StatusReembolso {
    ABERTO("aberto"),
    APROVADO_GESTOR("aprovado gestor"),
    FECHADO_PAGO("fechado(pago)"),
    REPROVADO_GESTOR("reprovado gestor"),
    REPROVADO_FINANCEIRO("reprovado financeiro"),
    TODOS("TODOS");
    private final String statusReembolso;

    StatusReembolso(String situacao) {
        this.statusReembolso = situacao;
    }

    public String getTipo() {
        return statusReembolso;
    }
}

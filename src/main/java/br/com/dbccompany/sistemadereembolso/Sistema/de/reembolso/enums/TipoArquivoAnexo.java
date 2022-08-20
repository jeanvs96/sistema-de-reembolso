package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums;

public enum TipoArquivoAnexo {
    PNG("image/png"),
    JPG("image/jpg"),
    JPEG("image/jpeg"),
    PDF("application/pdf");

    private final String tipoAnexo;

    TipoArquivoAnexo(String cargo) {
        this.tipoAnexo = cargo;
    }

    public String getTipo() {
        return tipoAnexo;
    }
}

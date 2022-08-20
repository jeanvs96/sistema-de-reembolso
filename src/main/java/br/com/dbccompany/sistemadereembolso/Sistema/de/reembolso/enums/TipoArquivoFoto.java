package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.enums;

public enum TipoArquivoFoto {
    PNG("image/png"),
    JPG("image/jpg"),
    JPEG("image/jpeg");

    private final String tipoFoto;

    TipoArquivoFoto(String cargo) {
        this.tipoFoto = cargo;
    }

    public String getTipo() {
        return tipoFoto;
    }
}

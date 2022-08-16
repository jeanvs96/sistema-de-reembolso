package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import lombok.Data;

@Data
public class UsuarioLoginComSucessoDTO {
    String token;
    String role;
}

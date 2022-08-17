package br.com.dbccompany.sistemadereembolso.Sistema.de.reembolso.dto.usuario;

import lombok.Data;

@Data
public class UsuarioLoginComSucessoDTO {
    Integer idUsuario;
    String token;
    String role;
}

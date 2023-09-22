package com.api.politizei.rest;

import com.api.politizei.base.IRestModel;
import com.api.politizei.model.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;

@Mapper.ReferenceModel(className = Usuario.class)
public class RestUsuario implements Serializable, IRestModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapper.RestId
    private String id;

    @NotEmpty(message = "Nome")
    @NotBlank(message = "Nome")
    private String usuNome;

    @NotEmpty(message = "Email")
    @NotBlank(message = "Email")
    private String usuEmail;

    @NotEmpty(message = "Perfil")
    @NotBlank(message = "Perfil")
    private String usuPerfil;

    private String perId;

    private RestPersona persona;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuNome() {
        return usuNome;
    }

    public void setUsuNome(String usuNome) {
        this.usuNome = usuNome;
    }

    public String getUsuEmail() {
        return usuEmail;
    }

    public void setUsuEmail(String usuEmail) {
        this.usuEmail = usuEmail;
    }

    public String getUsuPerfil() {
        return usuPerfil;
    }

    public void setUsuPerfil(String usuPerfil) {
        this.usuPerfil = usuPerfil;
    }

    public String getPerId() {
        return perId;
    }

    public void setPerId(String perId) {
        this.perId = perId;
    }

    public RestPersona getPersona() {
        return persona;
    }

    public void setPersona(RestPersona persona) {
        this.persona = persona;
    }
}

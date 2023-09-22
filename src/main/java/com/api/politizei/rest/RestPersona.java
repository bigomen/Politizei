package com.api.politizei.rest;

import com.api.politizei.base.IRestModel;
import com.api.politizei.model.Persona;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;

@Mapper.ReferenceModel(className = Persona.class)
public class RestPersona implements Serializable, IRestModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapper.RestId
    private String id;

    @NotEmpty(message = "Nome")
    @NotBlank(message = "Nome")
    private String perNome;

    @NotEmpty(message = "Alias")
    @NotBlank(message = "Alias")
    private String perAlias;

    @NotEmpty(message = "Partido")
    private String parId;

    private RestPartido partido;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPerNome() {
        return perNome;
    }

    public void setPerNome(String perNome) {
        this.perNome = perNome;
    }

    public String getPerAlias() {
        return perAlias;
    }

    public void setPerAlias(String perAlias) {
        this.perAlias = perAlias;
    }

    public String getParId() {
        return parId;
    }

    public void setParId(String parId) {
        this.parId = parId;
    }

    public RestPartido getPartido() {
        return partido;
    }

    public void setPartido(RestPartido partido) {
        this.partido = partido;
    }
}

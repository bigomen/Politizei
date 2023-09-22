package com.api.politizei.rest;

import com.api.politizei.base.IRestModel;
import com.api.politizei.model.Partido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

@Mapper.ReferenceModel(className = Partido.class)
public class RestPartido implements Serializable, IRestModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapper.RestId
    private String id;

    @NotEmpty(message = "Nome")
    @NotBlank(message = "Nome")
    private String parNome;

    @NotEmpty(message = "Número")
    @NotBlank(message = "Número")
    private String parNumero;

    private File parLogo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParNome() {
        return parNome;
    }

    public void setParNome(String parNome) {
        this.parNome = parNome;
    }

    public String getParNumero() {
        return parNumero;
    }

    public void setParNumero(String parNumero) {
        this.parNumero = parNumero;
    }

    public File getParLogo() {
        return parLogo;
    }

    public void setParLogo(File parLogo) {
        this.parLogo = parLogo;
    }
}

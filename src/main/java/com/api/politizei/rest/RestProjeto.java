package com.api.politizei.rest;

import com.api.politizei.base.IRestModel;
import com.api.politizei.model.Projeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;

@Mapper.ReferenceModel(className = Projeto.class)
public class RestProjeto implements IRestModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapper.RestId
    private String id;

    @NotEmpty(message = "Data")
    @NotBlank(message = "Data")
    private String prjData;

    @NotEmpty(message = "Titulo")
    @NotBlank(message = "Titulo")
    private String prjTitulo;

    @NotEmpty(message = "Persona")
    private String perId;

    private RestPersona persona;

    private String prjConteudo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrjData() {
        return prjData;
    }

    public void setPrjData(String prjData) {
        this.prjData = prjData;
    }

    public String getPrjTitulo() {
        return prjTitulo;
    }

    public void setPrjTitulo(String prjTitulo) {
        this.prjTitulo = prjTitulo;
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

    public String getPrjConteudo() {
        return prjConteudo;
    }

    public void setPrjConteudo(String prjConteudo) {
        this.prjConteudo = prjConteudo;
    }
}

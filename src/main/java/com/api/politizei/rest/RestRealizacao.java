package com.api.politizei.rest;

import com.api.politizei.base.IRestModel;
import com.api.politizei.model.Realizacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

@Mapper.ReferenceModel(className = Realizacao.class)
public class RestRealizacao implements IRestModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapper.RestId
    private String id;

    @NotEmpty(message = "Data")
    @NotBlank(message = "Data")
    private String relData;

    @NotEmpty(message = "Título")
    @NotBlank(message = "Título")
    private String relTitulo;

    @NotEmpty(message = "Persona")
    private String perId;

    private RestPersona persona;

    private File relFoto;

    private String relConteudo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelData() {
        return relData;
    }

    public void setRelData(String relData) {
        this.relData = relData;
    }

    public String getRelTitulo() {
        return relTitulo;
    }

    public void setRelTitulo(String relTitulo) {
        this.relTitulo = relTitulo;
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

    public File getRelFoto() {
        return relFoto;
    }

    public void setRelFoto(File relFoto) {
        this.relFoto = relFoto;
    }

    public String getRelConteudo() {
        return relConteudo;
    }

    public void setRelConteudo(String relConteudo) {
        this.relConteudo = relConteudo;
    }
}

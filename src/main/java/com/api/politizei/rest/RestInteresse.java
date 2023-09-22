package com.api.politizei.rest;

import com.api.politizei.base.IRestModel;
import com.api.politizei.model.Interesse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serial;
import java.io.Serializable;

@Mapper.ReferenceModel(className = Interesse.class)
public class RestInteresse implements IRestModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapper.RestId
    private String id;

    @NotEmpty(message = "Descrição")
    @NotBlank(message = "Descrição")
    private String intDescricao;

    @NotEmpty(message = "Persona")
    private String perId;

    private RestPersona persona;

    private String intConteudo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntDescricao() {
        return intDescricao;
    }

    public void setIntDescricao(String intDescricao) {
        this.intDescricao = intDescricao;
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

    public String getIntConteudo() {
        return intConteudo;
    }

    public void setIntConteudo(String intConteudo) {
        this.intConteudo = intConteudo;
    }
}

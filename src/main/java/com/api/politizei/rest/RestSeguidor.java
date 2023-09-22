package com.api.politizei.rest;

import com.api.politizei.base.IRestModel;
import com.api.politizei.model.Seguidor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Mapper.ReferenceModel(className = Seguidor.class)
public class RestSeguidor implements IRestModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Mapper.RestId
    private String id;

    @NotEmpty(message = "Nome")
    @NotBlank(message = "Nome")
    private String segNome;

    private String segEmail;

    @Size(max = 8, min = 8)
    private String segCep;

    @Size(max = 11)
    private String segWhatsapp;

    private LocalDate segDataNascimento;

    @NotEmpty(message = "Persona")
    private String perId;

    private RestPersona persona;

    private List<RestInteresse> interesseList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSegNome() {
        return segNome;
    }

    public void setSegNome(String segNome) {
        this.segNome = segNome;
    }

    public String getSegEmail() {
        return segEmail;
    }

    public void setSegEmail(String segEmail) {
        this.segEmail = segEmail;
    }

    public String getSegCep() {
        return segCep;
    }

    public void setSegCep(String segCep) {
        this.segCep = segCep;
    }

    public String getSegWhatsapp() {
        return segWhatsapp;
    }

    public void setSegWhatsapp(String segWhatsapp) {
        this.segWhatsapp = segWhatsapp;
    }

    public LocalDate getSegDataNascimento() {
        return segDataNascimento;
    }

    public void setSegDataNascimento(LocalDate segDataNascimento) {
        this.segDataNascimento = segDataNascimento;
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

    public List<RestInteresse> getInteresseList() {
        return interesseList;
    }

    public void setInteresseList(List<RestInteresse> interesseList) {
        this.interesseList = interesseList;
    }
}

package com.api.politizei.model;

import com.api.politizei.base.IDataBaseModel;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "SEGUIDOR")
@PageModel.SortField(fieldName = "segNome")
public class Seguidor extends PageModel implements IDataBaseModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SEG_ID")
    @Mapper.CryptoRequired
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SEGUIDOR")
    @SequenceGenerator(name = "SQ_SEGUIDOR", sequenceName = "SQ_SEGUIDOR", allocationSize = 1)
    private Long id;

    @Column(name = "SEG_NOME")
    private String segNome;

    @Column(name = "SEG_EMAIL")
    private String segEmail;

    @Column(name = "SEG_CEP")
    private String segCep;

    @Column(name = "SEG_WHATSAPP")
    private String segWhatsapp;

    @Column(name = "SEG_DATA_NASCIMENTO")
    private LocalDate segDataNascimento;

    @Mapper.CryptoRequired
    @Column(name = "PER_ID")
    private Long perId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PER_ID", referencedColumnName = "PER_ID", updatable = false, insertable = false)
    private Persona persona;

    @Column(name = "SEG_PUSH")
    private String segPush;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SEGUIDOR_INTERESSE",
            joinColumns = {@JoinColumn(name = "SEG_ID", referencedColumnName = "SEG_ID")},
            inverseJoinColumns = {@JoinColumn(name = "INT_ID", referencedColumnName = "INT_ID")})
    private List<Interesse> interesseList;

    public Seguidor() {
    }

    public Seguidor(Long id, String segNome) {
        this.id = id;
        this.segNome = segNome;
    }

    public Seguidor(Long id, String segNome, String segEmail, String perNome) {
        Persona per = new Persona();
        per.setPerNome(perNome);
        this.id = id;
        this.segNome = segNome;
        this.segEmail = segEmail;
        this.persona = per;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getPerId() {
        return perId;
    }

    public void setPerId(Long perId) {
        this.perId = perId;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getSegPush() {
        return segPush;
    }

    public void setSegPush(String segPush) {
        this.segPush = segPush;
    }

    public List<Interesse> getInteresseList() {
        return interesseList;
    }

    public void setInteresseList(List<Interesse> interesseList) {
        this.interesseList = interesseList;
    }
}

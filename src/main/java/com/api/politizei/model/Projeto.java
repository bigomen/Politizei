package com.api.politizei.model;

import com.api.politizei.base.IDataBaseModel;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "PROJETO")
@PageModel.SortField(fieldName = "prjTitulo")
public class Projeto extends PageModel implements IDataBaseModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRJ_ID")
    @Mapper.CryptoRequired
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PROJETO")
    @SequenceGenerator(name = "SQ_PROJETO", sequenceName = "SQ_PROJETO", allocationSize = 1)
    private Long id;

    @Column(name = "PRJ_DATA")
    private String prjData;

    @Column(name = "PRJ_TITULO")
    private String prjTitulo;

    @Mapper.CryptoRequired
    @Column(name = "PER_ID")
    private Long perId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PER_ID", referencedColumnName = "PER_ID", updatable = false, insertable = false)
    private Persona persona;

    @Column(name = "PRJ_CONTEUDO")
    private String prjConteudo;

    public Projeto() {
    }

    public Projeto(Long id, String prjTitulo) {
        this.id = id;
        this.prjTitulo = prjTitulo;
    }

    public Projeto(Long id, String prjTitulo, String perNome) {
        Persona per = new Persona();
        per.setPerNome(perNome);
        this.id = id;
        this.prjTitulo = prjTitulo;
        this.persona = per;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getPrjConteudo() {
        return prjConteudo;
    }

    public void setPrjConteudo(String prjConteudo) {
        this.prjConteudo = prjConteudo;
    }
}

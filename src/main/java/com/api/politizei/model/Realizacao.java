package com.api.politizei.model;

import com.api.politizei.base.IDataBaseModel;
import jakarta.persistence.*;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "REALIZACAO")
@PageModel.SortField(fieldName = "relTitulo")
public class Realizacao extends PageModel implements IDataBaseModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "REL_ID")
    @Mapper.CryptoRequired
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_REALIZACAO")
    @SequenceGenerator(name = "SQ_REALIZACAO", sequenceName = "SQ_REALIZACAO", allocationSize = 1)
    private Long id;

    @Column(name = "REL_DATA")
    private String relData;

    @Column(name = "REL_TITULO")
    private String relTitulo;

    @Mapper.CryptoRequired
    @Column(name = "PER_ID")
    private Long perId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PER_ID", referencedColumnName = "PER_ID", updatable = false, insertable = false)
    private Persona persona;

    @Column(name = "REL_FOTO")
    private File relFoto;

    @Column(name = "REL_CONTEUDO")
    private String relConteudo;

    public Realizacao() {
    }

    public Realizacao(Long id, String relTitulo) {
        this.id = id;
        this.relTitulo = relTitulo;
    }

    public Realizacao(Long id, String relTitulo, String perNome) {
        Persona per = new Persona();
        per.setPerNome(perNome);
        this.id = id;
        this.relTitulo = relTitulo;
        this.persona = per;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

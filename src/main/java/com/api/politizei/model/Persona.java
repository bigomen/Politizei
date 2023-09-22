package com.api.politizei.model;

import com.api.politizei.base.IDataBaseModel;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "PERSONA")
@PageModel.SortField(fieldName = "perNome")
public class Persona extends PageModel implements Serializable, IDataBaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PER_ID")
    @Mapper.CryptoRequired
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PERSONA")
    @SequenceGenerator(name = "SQ_PERSONA", sequenceName = "SQ_PERSONA", allocationSize = 1)
    private Long id;

    @Column(name = "PER_NOME")
    private String perNome;

    @Column(name = "PER_ALIAS")
    private String perAlias;

    @Mapper.CryptoRequired
    @Column(name = "PAR_ID")
    private Long parId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAR_ID", referencedColumnName = "PAR_ID", updatable = false, insertable = false)
    private Partido partido;

    public Persona() {
    }

    public Persona(Long id, String perNome) {
        this.id = id;
        this.perNome = perNome;
    }

    public Persona(Long id, String perNome, String perAlias, String parNome) {
        Partido par = new Partido();
        par.setParNome(parNome);
        this.id = id;
        this.perNome = perNome;
        this.perAlias = perAlias;
        this.partido = par;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getParId() {
        return parId;
    }

    public void setParId(Long parId) {
        this.parId = parId;
    }

    public Partido getPartido() {
        return partido;
    }

    public void setPartido(Partido partido) {
        this.partido = partido;
    }
}

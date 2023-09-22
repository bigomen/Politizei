package com.api.politizei.model;

import com.api.politizei.base.IDataBaseModel;
import jakarta.persistence.*;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "PARTIDO")
@PageModel.SortField(fieldName = "parNome")
public class Partido extends PageModel implements Serializable, IDataBaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PAR_ID")
    @Mapper.CryptoRequired
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PARTIDO")
    @SequenceGenerator(name = "SQ_PARTIDO", sequenceName = "SQ_PARTIDO", allocationSize = 1)
    private Long id;

    @Column(name = "PAR_NOME")
    private String parNome;

    @Column(name = "PAR_NUMERO")
    private String parNumero;

    @Column(name = "PAR_LOGO")
    private File parLogo;

    public Partido() {
    }

    public Partido(Long id, String parNome) {
        this.id = id;
        this.parNome = parNome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

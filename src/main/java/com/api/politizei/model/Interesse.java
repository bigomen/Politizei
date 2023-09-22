package com.api.politizei.model;

import com.api.politizei.base.IDataBaseModel;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "INTERESSE")
@PageModel.SortField(fieldName = "intDescricao")
public class Interesse extends PageModel implements IDataBaseModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INT_ID")
    @Mapper.CryptoRequired
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_INTERESSE")
    @SequenceGenerator(name = "SQ_INTERESSE", sequenceName = "SQ_INTERESSE", allocationSize = 1)
    private Long id;

    @Column(name = "INT_DESCRICAO")
    private String intDescricao;

    @Mapper.CryptoRequired
    @Column(name = "PER_ID")
    private Long perId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PER_ID", referencedColumnName = "PER_ID", updatable = false, insertable = false)
    private Persona persona;

    @Column(name = "INT_CONTEUDO")
    private String intConteudo;

    public Interesse() {
    }

    public Interesse(Long id, String intDescricao) {
        this.id = id;
        this.intDescricao = intDescricao;
    }

    public Interesse(Long id, String intDescricao, String perNome) {
        Persona per = new Persona();
        per.setPerNome(perNome);
        this.id = id;
        this.intDescricao = intDescricao;
        this.persona = per;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntDescricao() {
        return intDescricao;
    }

    public void setIntDescricao(String intDescricao) {
        this.intDescricao = intDescricao;
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

    public String getIntConteudo() {
        return intConteudo;
    }

    public void setIntConteudo(String intConteudo) {
        this.intConteudo = intConteudo;
    }
}

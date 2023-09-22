package com.api.politizei.model;

import com.api.politizei.base.IDataBaseModel;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
@PageModel.SortField(fieldName = "usuNome")
public class Usuario extends PageModel implements Serializable, IDataBaseModel {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USU_ID")
    @Mapper.CryptoRequired
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_USUARIO")
    @SequenceGenerator(name = "SQ_USUARIO", sequenceName = "SQ_USUARIO", allocationSize = 1)
    private Long id;

    @Column(name = "USU_NOME")
    private String usuNome;

    @Column(name = "USU_EMAIL")
    private String usuEmail;

    @Column(name = "USU_SENHA")
    private String usuSenha;

    @Column(name = "USU_TOKEN")
    private String usuToken;

    @Column(name = "USU_TOKEN_EXPIRACAO")
    private LocalDateTime usuTokenExpiracao;

    @Column(name = "USU_DATA_EXCLUSAO")
    private LocalDateTime usuDataExclusao;

    @Column(name = "USU_PERFIL")
    private String usuPerfil;

    @Mapper.CryptoRequired
    @Column(name = "PER_ID")
    private Long perId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PER_ID", referencedColumnName = "PER_ID", updatable = false, insertable = false)
    private Persona persona;

    public Usuario() {
    }

    public Usuario(Long id, String usuNome) {
        this.id = id;
        this.usuNome = usuNome;
    }

    public Usuario(Long id, String usuNome, String usuEmail, String usuPerfil, String perNome) {
        Persona per = new Persona();
        per.setPerNome(perNome);
        this.id = id;
        this.usuNome = usuNome;
        this.usuEmail = usuEmail;
        this.usuPerfil = usuPerfil;
        this.persona = per;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuNome() {
        return usuNome;
    }

    public void setUsuNome(String usuNome) {
        this.usuNome = usuNome;
    }

    public String getUsuEmail() {
        return usuEmail;
    }

    public void setUsuEmail(String usuEmail) {
        this.usuEmail = usuEmail;
    }

    public String getUsuSenha() {
        return usuSenha;
    }

    public void setUsuSenha(String usuSenha) {
        this.usuSenha = usuSenha;
    }

    public String getUsuToken() {
        return usuToken;
    }

    public void setUsuToken(String usuToken) {
        this.usuToken = usuToken;
    }

    public LocalDateTime getUsuTokenExpiracao() {
        return usuTokenExpiracao;
    }

    public void setUsuTokenExpiracao(LocalDateTime usuTokenExpiracao) {
        this.usuTokenExpiracao = usuTokenExpiracao;
    }

    public LocalDateTime getUsuDataExclusao() {
        return usuDataExclusao;
    }

    public void setUsuDataExclusao(LocalDateTime usuDataExclusao) {
        this.usuDataExclusao = usuDataExclusao;
    }

    public String getUsuPerfil() {
        return usuPerfil;
    }

    public void setUsuPerfil(String usuPerfil) {
        this.usuPerfil = usuPerfil;
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
}

package com.api.politizei.repository;

import com.api.politizei.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends PaginationRepository<Usuario>{

    Optional<Usuario> findByUsuEmail(String usuEmail);

    Boolean existsByUsuEmail(String usuEmail);

    @Query("select new Usuario(u.id, u.usuNome) from Usuario u where u.usuDataExclusao = null")
    List<Usuario> listaSimples();

    @Query("select CASE WHEN count(u.id) > 0 THEN true ELSE false END from Usuario u where u.usuEmail = :usuEmail and u.id <> :id")
    Boolean validarEmailAtualizacao(String usuEmail, Long id);

    @Query("select new Usuario(u.id, u.usuNome, u.usuEmail, u.usuPerfil, p.perNome) from Usuario u left join u.persona p where u.usuDataExclusao = null")
    Page<Usuario> findAll(Pageable pageable);

    @Modifying
    @Query("update Usuario u set u.usuDataExclusao = current_date where u.id = :id")
    void desativarUsuario(Long id);
}

package com.example.saltitantes.model.repository;

import java.util.Optional;

import com.example.saltitantes.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> deleteByLogin(String login);

}

package com.example.saltitantes.repository;

import com.example.saltitantes.model.entity.Usuario;

public interface UsuarioRepository extends org.springframework.data.jpa.repository.JpaRepository<Usuario, Long> {

    /**
     * Busca um usuário pelo email.
     *
     * @param email o email do usuário
     * @return o usuário encontrado ou null se não existir
     */
    Usuario findByLogin(String email);

}

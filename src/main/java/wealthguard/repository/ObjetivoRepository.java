package wealthguard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import wealthguard.entity.ObjetivoEntity;

@Repository
public interface ObjetivoRepository extends JpaRepository<ObjetivoEntity, Integer>{

    // Busca los objetivos de un usuario ordenados por fecha de inicio descendente
Optional<ObjetivoEntity> findFirstByUsuarioIdOrderByFechaInicioDesc(Integer idUsuario);

    

}


package wealthguard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wealthguard.entity.ObjetivoEntity;

@Repository
public interface ObjetivoRepository extends JpaRepository<ObjetivoEntity, Integer>{

    // Busca los objetivos activos del usuario
    @Query("SELECT o FROM ObjetivoEntity o WHERE o.usuario.id = :idUsuario AND o.completado = false")
    List<ObjetivoEntity> buscarObjetivosActivosPorUsuario(@Param("idUsuario") Integer idUsuario);

    

}


package wealthguard.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wealthguard.entity.TipoRecomendacionEntity;

@Repository
public interface TipoRecomendacionRepository extends JpaRepository<TipoRecomendacionEntity, Integer> {



}


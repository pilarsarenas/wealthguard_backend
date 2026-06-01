package wealthguard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wealthguard.entity.ScoreFinancieroEntity;
public interface ScoreFinancieroRepository extends JpaRepository<ScoreFinancieroEntity, Integer> {

}


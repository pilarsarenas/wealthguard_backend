package wealthguard.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import wealthguard.entity.ScoreFinancieroEntity;

@Repository
public interface ScoreFinancieroRepository extends JpaRepository<ScoreFinancieroEntity, Integer> {

	@Modifying
	@Transactional
	@Query("DELETE FROM ScoreFinancieroEntity s WHERE s.usuarioId.id = :usuarioId")
	void deleteByUsuarioId(@Param("usuarioId") Integer usuarioId);

}


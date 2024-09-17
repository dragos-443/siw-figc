package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;

public interface PresidentRepository extends CrudRepository<President, Long> {
	
	@Query("SELECT p.team FROM President p WHERE p.id = :presidentId")
    Team findTeamByPresidentId(@Param("presidentId") Long presidentId);
	
	public boolean existsByNameAndSurname(String name, String surname);
	
	public President findByUsername(String username);
	
	public President findByNameAndSurname(String name, String surname);
	
}

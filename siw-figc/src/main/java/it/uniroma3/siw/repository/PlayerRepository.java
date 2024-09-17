package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Player;


public interface PlayerRepository extends CrudRepository<Player, Long> {

	public boolean existsByNameAndLastname(String name, String lastname);
	
	@Query("SELECT p "
			+ "FROM Player p "
			+ "WHERE p.teamAppartenenza IS NULL ")
	public Iterable<Player> findPlayersNotInATeam();

}

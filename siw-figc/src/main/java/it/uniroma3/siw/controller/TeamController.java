package it.uniroma3.siw.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validator.PlayerValidator;
import it.uniroma3.siw.controller.validator.TeamValidator;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.TeamRepository;
import it.uniroma3.siw.repository.PlayerRepository;
import it.uniroma3.siw.repository.PresidentRepository;
import jakarta.validation.Valid;

@Controller
public class TeamController {
	@Autowired 
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;
	
	@Autowired
	private PresidentRepository presidentRepository;
	
	@Autowired 
	private TeamValidator teamValidator;
	
	@Autowired 
	private PlayerValidator playerValidator;
	
	//GOOD
	@GetMapping(value="/admin/formNewTeam")
	public String formNewTeam(Model model) {
		model.addAttribute("team", new Team());
		return "admin/formNewTeam.html";
	}
	
	//GOOD
	@GetMapping(value="/admin/formUpdateTeam/{id}")
	public String formUpdateTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", teamRepository.findById(id).get());
		return "admin/formUpdateTeam.html";
	}
	
	//PENSO GOOD - controllato ale
//	@PostMapping(value="/admin/updateTeam/{id}")
//	public String updateTeam(@PathVariable("id") Long id, Model model, @Valid @ModelAttribute("team") Team team, BindingResult bindingResult) {
//		this.teamValidator.validate(team, bindingResult);
//		if (!bindingResult.hasErrors()) {
//			Team teamDaAggiornare = teamRepository.findById(id).get();
//			//verificare prima se sono uguali
//			teamDaAggiornare.getPresident().setTeam(null); //tolgo collegamento al team dal vecchio presidente
//			System.out.println("EEEEEEEEEEEEEEEEEEEEEEE");
//			teamDaAggiornare.setIndirizzo(team.getIndirizzo());
//			teamDaAggiornare.setPresident(team.getPresident());
//			team.getPresident().setTeam(team);
//			
//			teamRepository.save(teamDaAggiornare);
//			presidentRepository.save(team.getPresident());
//			
//			model.addAttribute("allTeams", teamRepository.findAll());
//			model.addAttribute("allPlayers", playerRepository.findAll());
//			
//			return "admin/indexAdmin.html";
//		}
//		else {
//			model.addAttribute("team", teamRepository.findById(id).get());
//			return "/admin/formUpdateTeam.html";
//		}
//		
//	}
	
	
	@PostMapping(value="/admin/updateTeam/{id}")
	public String updateTeam(@PathVariable("id") Long id, Model model, @Valid @ModelAttribute("team") Team team
			/*@RequestParam("presidentName") String presidentName,
            @RequestParam("presidentSurname") String presidentSurname*/) {



	        // Recupera il team esistente dal repository
	        Team teamDaAggiornare = teamRepository.findById(id).orElse(null);
	        if (teamDaAggiornare == null) {
	            // Gestisci il caso in cui il team non esiste
	            model.addAttribute("errorMessage", "Il team non esiste.");
	            return "redirect:/admin/teamList";  // o reindirizza a una pagina di errore
	        }

	        // Se il presidente è cambiato
	        President presidenteAttuale = teamDaAggiornare.getPresident();
	        President nuovoPresidente = presidentRepository.findByNameAndSurname(team.getPresident().getName(), team.getPresident().getSurname());
	        // Verifica se il presidente è diverso dal precedente
	        if (!nuovoPresidente.equals(presidenteAttuale) && presidenteAttuale != null && nuovoPresidente!=null) {
	            // Rimuove il collegamento dal vecchio presidente
	            presidenteAttuale.setTeam(null);
	            presidentRepository.save(presidenteAttuale); // Salva la modifica sul vecchio presidente

	            // Associa il nuovo presidente al team
	            nuovoPresidente.setTeam(teamDaAggiornare);
	            teamDaAggiornare.setPresident(nuovoPresidente);
	            presidentRepository.save(nuovoPresidente); // Salva il nuovo presidente
	        }
	        else if(presidenteAttuale==null && nuovoPresidente!=null) {
	        	teamDaAggiornare.setPresident(nuovoPresidente);
	        	nuovoPresidente.setTeam(teamDaAggiornare);
	        	presidentRepository.save(nuovoPresidente);
	        }
	        // Aggiorna gli altri campi della squadra
	        teamDaAggiornare.setIndirizzo(team.getIndirizzo());
	        // Puoi aggiungere altre proprietà del team qui

	        // Salva il team aggiornato
	        teamRepository.save(teamDaAggiornare);

	        model.addAttribute("allTeams", teamRepository.findAll());
	        model.addAttribute("allPlayers", playerRepository.findAll());

	        return "admin/indexAdmin.html";
	}

	
	//GOOD
	@PostMapping(value="/admin/team")
	public String newTeam(@Valid @ModelAttribute("team") Team team, 
							BindingResult bindingResult,
							Model model,
							@RequestParam("nomePresidente") String nomePresidente,
							@RequestParam("cognomePresidente") String cognomePresidente) {
		
		this.teamValidator.validate(team, bindingResult);
		if (!bindingResult.hasErrors()) {
			
			President president = presidentRepository.findByNameAndSurname(nomePresidente, cognomePresidente);
			team.setPresident(president);
			president.setTeam(team);
			
			this.teamRepository.save(team);
			this.presidentRepository.save(president); 
			
			model.addAttribute("allTeams", teamRepository.findAll());
			model.addAttribute("allPlayers", playerRepository.findAll());
			return "admin/indexAdmin.html";
		} else {
			if (bindingResult.hasErrors()) {
			    // Logga gli errori per capire cosa sta succedendo
			    bindingResult.getAllErrors().forEach(error -> {
			        System.out.println(error.toString());
			    });
			}
			model.addAttribute("team", new Team());
			return "admin/formNewTeam.html"; 
		}
	}
	
	/* DA METTERE SU PLAYER CONTROLLER */
	
	//GOOD
	@GetMapping(value="/admin/deletePlayer/{id}")
	public String deletePlayer(@PathVariable("id") Long id, Model model) {
		playerRepository.deleteById(id);
		model.addAttribute("allTeams", teamRepository.findAll());
		model.addAttribute("allPlayers", playerRepository.findAll());
		return "admin/indexAdmin.html";
	}
	
	//PENSO GOOD
	@GetMapping(value="/admin/formNewPlayer")
	public String formNewPlayer(Model model) {
		model.addAttribute("player", new Player());
		return "admin/formNewPlayer.html";
	}
	
	//PENSO GOOD
	@PostMapping("/admin/player")
	public String newPlayer(@Valid @ModelAttribute("player") Player player, Model model, BindingResult bindingResult) {
		this.playerValidator.validate(player, bindingResult);
		if (!bindingResult.hasErrors()) {	
			this.playerRepository.save(player); 
			model.addAttribute("allTeams", teamRepository.findAll());
			model.addAttribute("allPlayers", playerRepository.findAll());
			return "admin/indexAdmin.html";
		} 
		else {
			model.addAttribute("player", new Player());
			return "admin/formNewPlayer.html"; 	
		}
		
	}
	
	
	/* Parte visualizzazione Teams*/
	
	//GOOD
	@GetMapping(value="/teams")
	public String manageTeams(Model model) {
		model.addAttribute("teams", this.teamRepository.findAll());
		return "teams.html";
	}
	
	@GetMapping("/team/{id}")
	public String getTeam(@PathVariable("id") Long id, Model model) {
		model.addAttribute("team", this.teamRepository.findById(id).get());
		return "team.html";
	}
	
	@GetMapping(value="/president/addPlayer/{idPlayer}/{idTeam}")
	public String addPlayer(Model model, @Valid @PathVariable("idPlayer") Long idPlayer, @Valid @PathVariable("idTeam") Long idTeam) {
		
		Player player = playerRepository.findById(idPlayer).get();
		player.setStartDate(LocalDate.now());
		player.setEndDate(null);

		Team team = teamRepository.findById(idTeam).get();
		player.setTeamAppartenenza(team);
		team.getPlayers().add(player);
		teamRepository.save(team);
		playerRepository.save(player);
		
		model.addAttribute("playersOfTheTeam", team.getPlayers());
		model.addAttribute("nomeSquadra", team.getNomeSquadra());
		model.addAttribute("team", team);
		model.addAttribute("freePlayers", playerRepository.findPlayersNotInATeam());
		
		return "president/indexPresident.html";
	}
	
	@GetMapping(value="/president/removePlayer/{idPlayer}/{idTeam}")
	public String removePlayer(Model model, @Valid @PathVariable("idPlayer") Long idPlayer, @Valid @PathVariable("idTeam") Long idTeam){
		
		Player player = playerRepository.findById(idPlayer).get();
		player.setEndDate(LocalDate.now());
		
		Team team = teamRepository.findById(idTeam).get();
		team.getPlayers().remove(player);
		player.setTeamAppartenenza(null);
		
		playerRepository.save(player);
		teamRepository.save(team);
		
		model.addAttribute("playersOfTheTeam", team.getPlayers());
		model.addAttribute("nomeSquadra", team.getNomeSquadra());
		model.addAttribute("team", team);
		model.addAttribute("freePlayers", playerRepository.findPlayersNotInATeam());
		
		return "president/indexPresident.html";
	}
	
	
	//GOOD
	@GetMapping(value="/players")
	public String viewPlayers(Model model) {
		model.addAttribute("players", this.playerRepository.findAll());
		return "players.html";
	}
	
	@GetMapping("/player/{id}")
	public String getPlayer(@PathVariable("id") Long id, Model model) {
		model.addAttribute("player", this.playerRepository.findById(id).get());
		return "player.html";
	}
	
	
	//GOOD
		@GetMapping(value="/presidents")
		public String viewPresidents(Model model) {
			model.addAttribute("presidents", this.presidentRepository.findAll());
			return "presidents.html";
		}
		
		@GetMapping("/president/{id}")
		public String getPresident(@PathVariable("id") Long id, Model model) {
			model.addAttribute("president", this.presidentRepository.findById(id).get());
			return "president.html";
		}
}
	
	
	
	
	
	/*-----------------------------------------------------------------------------*/
	/*-----------------------------------------------------------------------------*/
	/*-----------------------------------------------------------------------------*/
	
	
	
	
//	
//	@GetMapping(value="/admin/setDirectorToTeam/{directorId}/{teamId}")
//	public String setDirectorToTeam(@PathVariable("directorId") Long directorId, @PathVariable("teamId") Long teamId, Model model) {
//		
//		Artist director = this.artistRepository.findById(directorId).get();
//		Team team = this.teamRepository.findById(teamId).get();
//		team.setDirector(director);
//		this.teamRepository.save(team);
//		
//		model.addAttribute("team", team);
//		return "admin/formUpdateTeam.html";
//	}
//	
//	
//	@GetMapping(value="/admin/addDirector/{id}")
//	public String addDirector(@PathVariable("id") Long id, Model model) {
//		model.addAttribute("artists", artistRepository.findAll());
//		model.addAttribute("team", teamRepository.findById(id).get());
//		return "admin/directorsToAdd.html";
//	}
//
//	
//	@GetMapping("/formSearchTeams")
//	public String formSearchTeams() {
//		return "formSearchTeams.html";
//	}
//
//	@PostMapping("/searchTeams")
//	public String searchTeams(Model model, @RequestParam int year) {
//		model.addAttribute("teams", this.teamRepository.findByYear(year));
//		return "foundTeams.html";
//	}
//	
//	@GetMapping("/admin/updateActors/{id}")
//	public String updateActors(@PathVariable("id") Long id, Model model) {
//
//		List<Artist> actorsToAdd = this.actorsToAdd(id);
//		model.addAttribute("actorsToAdd", actorsToAdd);
//		model.addAttribute("team", this.teamRepository.findById(id).get());
//
//		return "admin/actorsToAdd.html";
//	}
//
//	@GetMapping(value="/admin/addActorToTeam/{actorId}/{teamId}")
//	public String addActorToTeam(@PathVariable("actorId") Long actorId, @PathVariable("teamId") Long teamId, Model model) {
//		Team team = this.teamRepository.findById(teamId).get();
//		Artist actor = this.artistRepository.findById(actorId).get();
//		Set<Artist> actors = team.getActors();
//		actors.add(actor);
//		this.teamRepository.save(team);
//		
//		List<Artist> actorsToAdd = actorsToAdd(teamId);
//		
//		model.addAttribute("team", team);
//		model.addAttribute("actorsToAdd", actorsToAdd);
//
//		return "admin/actorsToAdd.html";
//	}
//	
//	@GetMapping(value="/admin/removeActorFromTeam/{actorId}/{teamId}")
//	public String removeActorFromTeam(@PathVariable("actorId") Long actorId, @PathVariable("teamId") Long teamId, Model model) {
//		Team team = this.teamRepository.findById(teamId).get();
//		Artist actor = this.artistRepository.findById(actorId).get();
//		Set<Artist> actors = team.getActors();
//		actors.remove(actor);
//		this.teamRepository.save(team);
//
//		List<Artist> actorsToAdd = actorsToAdd(teamId);
//		
//		model.addAttribute("team", team);
//		model.addAttribute("actorsToAdd", actorsToAdd);
//
//		return "admin/actorsToAdd.html";
//	}
//
//	private List<Artist> actorsToAdd(Long teamId) {
//		List<Artist> actorsToAdd = new ArrayList<>();
//
//		for (Artist a : artistRepository.findActorsNotInTeam(teamId)) {
//			actorsToAdd.add(a);
//		}
//		return actorsToAdd;
//	}
//}

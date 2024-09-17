package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Player;
import it.uniroma3.siw.model.President;
import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.PlayerRepository;
import it.uniroma3.siw.repository.PresidentRepository;
import it.uniroma3.siw.repository.TeamRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	
	//TUTTO FATTO
	
	@Autowired
	private PresidentRepository presidentRepository;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private PlayerRepository playerRepository;

    @Autowired
	private UserService userService;
	
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		model.addAttribute("president", new President());
		return "formRegisterUser";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin";
	}

	@GetMapping(value = "/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				model.addAttribute("allTeams", teamRepository.findAll());
				model.addAttribute("allPlayers", playerRepository.findAll());
				return "admin/indexAdmin.html";
			}
			if (credentials.getRole().equals(Credentials.PRESIDENT_ROLE)) {
				President presidente = presidentRepository.findByUsername(userDetails.getUsername());
				model.addAttribute("nomeSquadra", presidente.getTeam().getNomeSquadra());
				//model.addAttribute("team", presidente.getTeam());
				model.addAttribute("playersOfTheTeam", presidente.getTeam().getPlayers());
				model.addAttribute("freePlayers", playerRepository.findPlayersNotInATeam());
				return "president/indexPresident.html";
			}
		}
        return "index.html";
	}
		
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("allTeams", teamRepository.findAll());
			model.addAttribute("allPlayers", playerRepository.findAll());
            return "admin/indexAdmin.html";
        }
    	if (credentials.getRole().equals(Credentials.PRESIDENT_ROLE)) {
    		President presidente = presidentRepository.findByUsername(credentials.getUsername());
			model.addAttribute("nomeSquadra", presidente.getTeam().getNomeSquadra());
			model.addAttribute("team", presidente.getTeam());
			model.addAttribute("playersOfTheTeam", presidente.getTeam().getPlayers());
			model.addAttribute("freePlayers", playerRepository.findPlayersNotInATeam());
            return "president/indexPresident.html";
        }
        return "index.html";
    }

	@PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 @ModelAttribute("president") President president,
                 BindingResult credentialsBindingResult,
                 Model model) {

		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            userService.saveUser(user);
            credentials.setUser(user);
            
            if(president.getCodiceFiscale()!="") {
            	president.setName(user.getName());
                president.setSurname(user.getSurname());
                president.setUsername(credentials.getUsername());
                presidentRepository.save(president);
                credentials.setRole(Credentials.PRESIDENT_ROLE);
            }
            
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        return "registerUser";
    }
}
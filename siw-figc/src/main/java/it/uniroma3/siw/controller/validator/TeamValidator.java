package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Team;
import it.uniroma3.siw.repository.TeamRepository;

@Component
public class TeamValidator implements Validator {
	@Autowired
	private TeamRepository teamRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Team team = (Team)o;
		if (team.getNomeSquadra()!=null && team.getYear()!=null 
				&& teamRepository.existsByNomeSquadraAndYear(team.getNomeSquadra(), team.getYear())) {
			errors.reject("team.duplicate", "doppione!");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Team.class.equals(aClass);
	}
}
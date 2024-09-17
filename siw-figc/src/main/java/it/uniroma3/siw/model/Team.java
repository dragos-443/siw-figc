package it.uniroma3.siw.model;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Team {
    
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @NotBlank
        private String nomeSquadra;
        
        @NotNull
        @Min(1850)
        @Max(2024)
        private Integer year;
        
        private String indirizzo;
        

		private String urlImage; //devo fare una gestione delle immagini
		
		@OneToOne(mappedBy = "team")
		private President president;
		

		@OneToMany(mappedBy="teamAppartenenza")
		private Set<Player> players;
       
    
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getNomeSquadra() {
            return nomeSquadra;
        }
        public void setNomeSquadra(String nomeSquadra) {
            this.nomeSquadra = nomeSquadra;
        }
    
        public Integer getYear() {
            return year;
        }
    
        public void setYear(Integer year) {
            this.year = year;
        }
        
        public String getIndirizzo() {
        	return indirizzo;
        }
        
        public void setIndirizzo(String indirizzo) {
        	this.indirizzo = indirizzo;
        }
        
        public String getUrlImage() {
            return urlImage;
        }
    
        public void setUrlImage(String urlImage) {
            this.urlImage = urlImage;
        }
        
        public President getPresident() {
        	return president;
        }
        
        public void setPresident(President president) {
        	this.president = president;
        }
        
        public Set<Player> getPlayers() {
        	return players;
        }
        
        public void setPlayers(Set<Player> players) {
        	this.players = players;
        }
		 
    
        @Override
        public int hashCode() {
            return Objects.hash(nomeSquadra, year);
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Team other = (Team) obj;
            return Objects.equals(nomeSquadra, other.nomeSquadra) && year.equals(other.year);
        }
    }

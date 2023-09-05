package it.uniroma3.siw.model;

import java.util.ArrayList;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
public class Fornitore {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String nome;

	@NotNull
	private String indirizzo;

	@NotNull
	private String email;
	
	@ManyToMany
	private List<Prodotto> prodotti= new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}

	public void setNome(String name) {
		this.nome = name;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String ind) {
		this.indirizzo = ind;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<Prodotto> getProdotti() {
		return prodotti;
	}

	public void setProdotti(List<Prodotto> prod) {
		this.prodotti = prod;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(nome, email);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fornitore other = (Fornitore) obj;
		return Objects.equals(nome, other.nome) && Objects.equals(email, other.email);
	}

	public void setProdotto(Prodotto prod) {
		this.prodotti.add(prod);	
	}

}
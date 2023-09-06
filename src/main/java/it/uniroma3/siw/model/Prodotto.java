package it.uniroma3.siw.model;

import java.util.ArrayList;


import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;



@Entity
public class Prodotto {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String nome;

	@NotNull
	private Float prezzo;

	@NotNull
	private String descrizione;
	
	@ManyToMany(mappedBy="prodotti")
	private List<Fornitore> fornitori= new ArrayList<>();
	
	@OneToMany(mappedBy="prodotto")
	private List<Commento> commenti= new ArrayList<>();
	
	@Min(2)
	@Max(4)
	@OneToMany(mappedBy="prodotto",cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Image> images=new ArrayList<>();
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Float getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(Float prezzo) {
		this.prezzo = prezzo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String desc) {
		this.descrizione = desc;
	}
	
	public List<Fornitore> getFornitori() {
		return fornitori;
	}

	public void setFornitori(List<Fornitore> forn) {
		this.fornitori = forn;
	}
	
	public List<Commento> getCommenti() {
		return commenti;
	}

	public void setCommenti(List<Commento> com) {
		this.commenti = com;
	}
	
	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> image) {
		this.images = image;
	}
	
	public void addImage(Image image) {
		this.images.add(image);
	}
	
	public String getFirstImage() {
		if(images.isEmpty())
			return null;
		else return this.images.get(0).getBase64Image();
	}
	
	public Long getFirstImageId() {
		if(images.isEmpty())
			return null;
		else return this.images.get(0).getId();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(nome, prezzo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prodotto other = (Prodotto) obj;
		return Objects.equals(nome, other.nome)&&Objects.equals(prezzo, other.prezzo);
	}
	


}
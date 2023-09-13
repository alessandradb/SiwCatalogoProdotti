package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Fornitore;
import it.uniroma3.siw.model.Prodotto;


public interface FornitoreRepository extends CrudRepository<Fornitore, Long> {
	
	public Fornitore findByNome(String nome);
	
	public List<Fornitore> findByProdottiNotContaining(Prodotto p);
	
	public List<Fornitore> findAll();
	
	public boolean existsByNomeAndEmail(String nome,String email);
	
}
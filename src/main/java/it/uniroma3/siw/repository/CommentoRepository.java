package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Fornitore;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;


public interface CommentoRepository extends CrudRepository<Commento, Long> {

	public List<Commento> findByProdottoAndUserNot(Prodotto prod, User user);

	public Commento findByProdottoAndUser(Prodotto prod, User user);
	
	public boolean existsByProdottoAndUser(Prodotto prod,User user);
}
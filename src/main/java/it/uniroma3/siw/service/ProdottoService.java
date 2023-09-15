package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;




import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Fornitore;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.repository.CommentoRepository;
import it.uniroma3.siw.repository.FornitoreRepository;
import it.uniroma3.siw.repository.ProdottoRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.TransactionScoped;
import javax.validation.Valid;

/**

 */
@Service
public class ProdottoService {
	
	@Autowired 
	private ProdottoRepository prodottoRepository;
	
	@Autowired 
	private CommentoRepository commentoRepository;
	
	@Autowired 
	private FornitoreRepository fornitoreRepository;
	
	@Autowired
	private ImageService imageService;

	@Transactional
	public void saveProdotto(Prodotto prod) {
		this.prodottoRepository.save(prod);
		
	}
	
	@Transactional
	public List<Prodotto> allProdotti(){
		return this.prodottoRepository.findAll();
	}
	
	@Transactional
	public Prodotto findProdottoById(Long id) {
		return this.prodottoRepository.findById(id).get();
	}
	
	@Transactional
	public void removeProdotto(Prodotto prod) {
		if(!prod.getCommenti().isEmpty()) {
			for(Commento com:prod.getCommenti())
				this.commentoRepository.delete(com);
		}
		if(!prod.getFornitori().isEmpty()) {
			for(Fornitore forn :prod.getFornitori()) {
				forn.getProdotti().remove(prod);
				this.fornitoreRepository.save(forn);
			}	
		}
		this.prodottoRepository.delete(prod);
	}
	
	@Transactional
	public List<Prodotto> findProdottoByNome(String nome){
		return this.prodottoRepository.findByNome(nome);
	}
	
	@Transactional
	public List<Prodotto> findProdottoByPrezzo(Float prezzo){
		return this.prodottoRepository.findByPrezzoLessThanEqual(prezzo);
	}
	
	@Transactional
	public List<Prodotto> findProdottoByFornitore(String nome){
		return this.prodottoRepository.findByFornitori(nome);
	}
	
	@Transactional
	public void newImagesProd(MultipartFile[] files, Prodotto prodotto) throws IOException {
		   if (files != null && files.length > 0 && !files[0].isEmpty()) {
	        for (MultipartFile file : files) {
	            byte[] imageData = file.getBytes();
	            String imageName = file.getOriginalFilename();

	            Image image = new Image();
	            image.setName(imageName);
	            image.setBytes(imageData);
	            image.setProdotto(prodotto);
	            prodotto.addImage(image);
	        }

	        this.imageService.saveAllImage(prodotto.getImages());
	    }
	}

	@Transactional
	public List<Prodotto> findProdottoByNomeOFornitore(String param) {
		return this.prodottoRepository.findByNomeOrFornitoriNome(param,param);
	}
	
	
}

package it.uniroma3.siw.controller;




import java.util.Set;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.controller.validation.CommentoValidator;
import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.Fornitore;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.service.UserService;


@Controller
public class CommentoController {
	
	@Autowired 
	private CommentoService commentoService;
	
	@Autowired 
	private CommentoValidator commentoValidator;
	
	@Autowired
	private ProdottoService prodottoService;
	
	@Autowired
	private UserService userService;
	
	

	@GetMapping("/formNewCommento/{prodId}")
	public String formNewCommento(@PathVariable("prodId") Long prodId,Model model) {
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		if(prod!=null) {
			model.addAttribute("commento", new Commento());
			model.addAttribute("prodotto", prod);
		}
		return "formNewCommento.html";
	}
	
	@PostMapping("/newCommento/{prodId}")
	public String newCommento(@Valid @ModelAttribute("prodotto") Commento com, BindingResult bindingResult, 
			@PathVariable("prodId") Long prodId, Model model) {
		
		Prodotto prod = this.prodottoService.findProdottoById(prodId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userService.getUserAuthentication(authentication);
		if(prod!=null && user!=null) {
			com.setProdotto(prod);
			com.setUser(user);
			this.commentoValidator.validate(com, bindingResult);
			if (!bindingResult.hasErrors()) {
				this.commentoService.saveCommento(com, prod, user);
			}
			else
				return "formNewCommento.html";
		}
		
		return "index.html";
	}
	
	@GetMapping("/rimuoviCommento/{comId}")
	public String removeCommento(@PathVariable("comId") Long comId, Model model) {
		Commento com=this.commentoService.findCommentoById(comId);
		this.commentoService.rimuoviCommento(com);
		return "index.html";
		
	}
	
	@PostMapping("/updateNome/{comId}")
	public String modificaNome(Model model, @RequestParam String nome,@PathVariable("comId") Long comId) {
		
		Commento comm = this.commentoService.findCommentoById(comId);
		comm.setNome(nome);
		this.commentoService.saveCommento(comm);
		model.addAttribute("commento", comm);
		
		return "updateCommento.html";
	}
	
	@PostMapping("/updateDescrizione/{comId}")
	public String modificaDescrizione(Model model, @RequestParam String descrizione,@PathVariable("comId") Long comId) {
		
		Commento comm = this.commentoService.findCommentoById(comId);
		comm.setDescrizione(descrizione);
		this.commentoService.saveCommento(comm);
		model.addAttribute("commento", comm);
		
		return "updateCommento.html";
	}
	


}

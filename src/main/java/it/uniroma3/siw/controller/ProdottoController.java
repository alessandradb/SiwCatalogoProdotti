package it.uniroma3.siw.controller;



import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.controller.validation.ProdottoValidator;
import it.uniroma3.siw.model.Categoria;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.ProdottoService;



@Controller
public class ProdottoController {
	
	@Autowired
	private ProdottoService prodottoService;
	
	@Autowired
	private ProdottoValidator prodottoValidator;
	
	@GetMapping("/admin/formNewProdotto")
	public String formNewProdotto(Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "admin/formNewProdotto.html";
	}
	
	@PostMapping("/admin/newProdotto")
	public String newProdotto(@Valid @ModelAttribute("prodotto") Prodotto prod, BindingResult bindingResult, 
			@RequestParam("file") MultipartFile file, Model model) throws IOException {
		
		this.prodottoValidator.validate(prod, bindingResult);
		
		if (!bindingResult.hasErrors()) {
			this.prodottoService.saveProdotto(prod);
			//this.prodottoService.newImagesCat(file, prod);

			model.addAttribute("prodotti", this.prodottoService.allProdotti());
			return "admin/adminProdotti.html";
		}
		else {
			return "/admin/formNewProdotto";
		}
	}
	
	

}

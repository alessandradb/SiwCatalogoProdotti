package it.uniroma3.siw.service;

import java.util.List;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ImageRepository;

@Service
public class ImageService {
	
	@Autowired
	private ImageRepository imageRepository;
	
	@Transactional
	public Image getImage(Long id) {
		if (id==0) return null;
		else return this.imageRepository.findById(id).get();
	}
	
	@Transactional
	public void saveImage(Image image) {
		this.imageRepository.save(image);
	}
	
	@Transactional
	public void saveAllImage(List<Image> images) {
		this.imageRepository.saveAll(images);
	}
	

}

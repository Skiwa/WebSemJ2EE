/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.dao.SempicAlbumFacade;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicAlbum;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class CreateAlbum implements Serializable {
    
    private SempicAlbum current;
    
    @Inject
    private SempicAlbumFacade albumDao;

    public CreateAlbum() {
    }
    
    @PostConstruct
    public void init() {
        current=new SempicAlbum();
    }

    public SempicAlbum getCurrent() {
        return current;
    }
    
    public void setTitle(String title){
       this.current.setTitle(title);
    }

    public void setCurrent(SempicAlbum current) {
        this.current = current;
    }
    
    public String create() throws SempicModelException {
       
        albumDao.create(current);       
        
        return "success";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.dao.SempicAlbumFacade;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicAlbum;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@RequestScoped
public class ShowAlbum {

    private SempicAlbum album;
    
    @Inject
    private SempicAlbumFacade albumDao;

   public ShowAlbum() {
    }
    
    public SempicAlbum getAlbum() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        int idAlbum = Integer.parseInt(request.getParameter("idAlbum"));
        
        if (album == null) {
            album = albumDao.findById(idAlbum);
        }
        return album;
    }
}

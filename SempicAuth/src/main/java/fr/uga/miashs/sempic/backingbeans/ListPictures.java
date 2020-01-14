/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.dao.SempicAlbumFacade;
import fr.uga.miashs.sempic.dao.SempicPictureFacade;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicAlbum;
import fr.uga.miashs.sempic.entities.SempicPicture;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@RequestScoped
public class ListPictures {

    @Inject
    private SempicPictureFacade pictureDao;
    @Inject
    private SempicAlbumFacade albumDao;
    
    public List<SempicPicture> getPictures() {
        return pictureDao.findAll();
    }
    public Set<SempicPicture> getPicturesByAlbumId(Long idAlbum) {
        return albumDao.read(idAlbum).getPictures();
    }
    
}

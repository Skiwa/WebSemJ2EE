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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@RequestScoped
public class ListAlbums {

    private List<SempicAlbum> dataModel;
    private List<SempicAlbum> albums;
    private int albumLength =0;
    
    @Inject
    private SempicAlbumFacade albumDao;
    
    @Inject     
    private SempicUserFacade userDao;

    
    public List<SempicAlbum> getAlbums() {
        if (albums == null) {
            dataModel = albumDao.findAll();
        }
        return dataModel;
    }
    
    public List<SempicAlbum> getAlbumsUser(SempicUser user){
        if (albums == null) {
            dataModel = albumDao.findAlbumsForUser(user.getId());
        }
        return dataModel;
    }
    
    public int getAlbumLength(SempicAlbum album){
        if(album.getPictures().size() == 0){
            return 0;
        }else {
            return album.getPictures().size();
        }
    }
    
}

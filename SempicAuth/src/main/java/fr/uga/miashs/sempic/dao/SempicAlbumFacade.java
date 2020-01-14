/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.SempicAlbum;
import fr.uga.miashs.sempic.entities.SempicUser;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */

@Stateless
public class SempicAlbumFacade extends AbstractJpaFacade<Long,SempicAlbum> {
    
    @Inject
    private SempicUserFacade userDao;
    
    public SempicAlbumFacade() {
        super(SempicAlbum.class);
    }

    @Override
    public Long create(SempicAlbum album) throws SempicModelException {
        return super.create(album);
    }
    
    @Override
    public List<SempicAlbum> findAll() {
        return getEntityManager().createQuery(this.findAllQuery())
            .getResultList();
    }
    
    public List<SempicAlbum> findAlbumsForUser(Long id){
        SempicUser user = userDao.read(id);
        List<SempicAlbum> albumsList = new ArrayList<>(user.getAlbums());
        
        return albumsList;
    }
}

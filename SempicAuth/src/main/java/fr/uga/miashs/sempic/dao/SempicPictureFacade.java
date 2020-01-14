/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.dao;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.entities.SempicPicture;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */

@Stateless
public class SempicPictureFacade extends AbstractJpaFacade<Long,SempicPicture> {

    
    public SempicPictureFacade() {
        super(SempicPicture.class);
    }

    @Override
    public Long create(SempicPicture picture) throws SempicModelException {
        return super.create(picture);
    }
    
    @Override
    public List<SempicPicture> findAll() {
        return getEntityManager().createQuery(this.findAllQuery())
            .getResultList();
    }
}

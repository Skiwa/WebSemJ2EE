/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */

@Entity
/*@Table(name="SempicAlbums", uniqueConstraints = {
    @UniqueConstraint(name="DistinctTitleForUser", columnNames = {"title","owner_id"})
})*/
public class SempicAlbum implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank(message="Un titre doit être donné")
    //@Column(name="title")
    private String title;
    
    
    //idem en one to many pour les photos
    
   /* @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="creatorId")
    @Column(name="photos")
    private SempicUser creator;

    public SempicUser getCreator() {
        return creator;
    }

    public void setCreator(SempicUser creator) {
        this.creator = creator;
    }*/

    
    public SempicAlbum() {
    }
    
    public long getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

   
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SempicAlbum other = (SempicAlbum) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

 
    @Override
    public String toString() {
        return "SempicAlbum{id="+ id + ", "
                + "title=" + title +'}';
    }
}

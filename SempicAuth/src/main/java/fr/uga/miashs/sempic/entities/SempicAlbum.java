/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */

@Entity
public class SempicAlbum implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank(message="Un titre doit être donné")
    private String title;
     
    @NotNull
    @ManyToOne
    @JoinColumn(name="userId")
    private SempicUser creator;
    
   @OneToMany(fetch = FetchType.EAGER, mappedBy = "album",cascade = CascadeType.REMOVE)
   private Set<SempicPicture> pictures;

    public Set<SempicPicture> getPictures() {
        return pictures;
    }

    public void setPictures(Set<SempicPicture> pictures) {
        this.pictures = pictures;
    }


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
    
    public SempicUser getCreator() {
        return creator;
    }
    
    public void setCreator(SempicUser creator) {
        this.creator = creator;
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
                + "title=" + title +","
                + "pictures=" + pictures + "}";
    }
}


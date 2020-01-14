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


/*@NamedQueries({
    @NamedQuery(
            name="findAllPicturesByAlbum",
            query="SELECT picture FROM SempicPicture WHERE picture.album=:album"
    )
})*/


@Entity
@Table(name="SempicPicture")
public class SempicPicture implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotBlank(message="Un titre doit être donné")
    private String title;
 
    @ManyToOne()
    @JoinColumn(name="albumId")
    private SempicAlbum album;

    public SempicAlbum getAlbum() {
        return album;
    }

    public void setAlbum(SempicAlbum album) {
        this.album = album;
    }
    
    public SempicPicture() {
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
        final SempicPicture other = (SempicPicture) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

 
    @Override
    public String toString() {
        return "SempicPicture{id="+ id + ", "
                + "title=" + title +'}';
    }
}
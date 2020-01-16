/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.SempicModelException;
import fr.uga.miashs.sempic.SempicModelUniqueException;
import fr.uga.miashs.sempic.dao.SempicAlbumFacade;
import fr.uga.miashs.sempic.dao.SempicPictureFacade;
import fr.uga.miashs.sempic.dao.SempicUserFacade;
import fr.uga.miashs.sempic.entities.SempicAlbum;
import fr.uga.miashs.sempic.entities.SempicPicture;
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import fr.uga.miashs.sempic.rdf.BasicSempicRDFStore;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.Context;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.servlet.http.Part;
import javax.validation.constraints.NotBlank;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDFS;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named
@ViewScoped
public class CreatePicture implements Serializable {
    
    private SempicPicture current;
    
    @Inject
    private SempicPictureFacade pictureDao;
    @Inject
    private SempicAlbumFacade albumDao;

    private boolean upladed;
    private Part image;
    
    private File myPicture;
    
    public CreatePicture() {
    }
    
    @PostConstruct
    public void init() {
        current=new SempicPicture();
    }

    public SempicPicture getCurrent() {
        return current;
    }
    
    public void setTitle(String title){
       this.current.setTitle(title);
    }

    public void setCurrent(SempicPicture current) {
        this.current = current;
    }
        public Part getImage() {
        return image;
    }

    public void setImage(Part image) {
        this.image = image;
    }

    public boolean isUpladed() {
        return upladed;
    }

    public void setUpladed(boolean upladed) {
        this.upladed = upladed;
    }

    public String create() throws SempicModelException {
        
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String idAlbum = params.get("idAlbum");
        
        SempicAlbum album = albumDao.read(Long.parseLong(idAlbum));
       
        try{
            InputStream in=image.getInputStream();
            System.out.println("My img: "+image);
            myPicture=new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")+image.getSubmittedFileName());
            myPicture.createNewFile();
            FileOutputStream out=new FileOutputStream(myPicture);
            System.out.println("My file: "+myPicture.getAbsoluteFile());
            byte[] buffer=new byte[1024];
            int length;
            
            while((length=in.read(buffer))>0){
                out.write(buffer, 0, length);
            }
            
            out.close();
            in.close();
            
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("path", myPicture.getAbsolutePath());
            upladed=true;
            
        }catch(Exception e){
            e.printStackTrace(System.out);
        }
       
        
       current.setAlbum(album);
       System.out.println("CON DE T MORT: "+myPicture);
       current.setImage(myPicture);
       pictureDao.create(current);
        
        return "show-album?faces-redirect=true&idAlbum=" + idAlbum;

    }
}

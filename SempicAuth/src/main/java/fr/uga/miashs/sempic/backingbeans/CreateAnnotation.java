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
import fr.uga.miashs.sempic.entities.SempicUser;
import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import fr.uga.miashs.sempic.rdf.BasicSempicRDFStore;
import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_GSP;
import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_QUERY;
import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_UPDATE;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
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
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

/**
 *
 * @author Jerome David <jerome.david@univ-grenoble-alpes.fr>
 */
@Named("createAnnotation")
@RequestScoped
public class CreateAnnotation {
    
    @Inject
    private SempicPictureFacade pictureDao;

   public CreateAnnotation() {
    }
    
    public String createAnnotation(SempicUser currentUser) throws SempicModelException {

        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String idAlbum = params.get("idAlbum"); 
        String idPicture = params.get("idPicture");        
        BasicSempicRDFStore s = new BasicSempicRDFStore();
        Model m = ModelFactory.createDefaultModel();

        System.out.println("CURRENT USER" + currentUser.getId());
        //Crée une photo
        //(id picture, id album, id owner)
        Resource pRes = s.createPicture(Long.parseLong(idPicture), Long.parseLong(idAlbum), currentUser.getId());          
        //Lie Manuel à la photo
        m.add(pRes, SempicOnto.subject, SempicOnto.Manuel_Atencia);
        
        //enregistre
        s.saveModel(m);
         return "show-album?faces-redirect=true&idAlbum="+idAlbum;
    }

    public List<String> getPictures() {
        ArrayList<String> list=new ArrayList<String>();
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        QueryExecution qe = cnx.query("SELECT DISTINCT ?s WHERE {?s a <https://example.com/ontology#Picture>}");
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
             QuerySolution qs = rs.next();
             //System.out.println(qs.getResource("s"));
             list.add(qs.getResource("s").toString());
         }
        cnx.close();
        return list;
    }
    public List<String> getPersons() {
        ArrayList<String> list=new ArrayList<String>();
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        QueryExecution qe = cnx.query("SELECT DISTINCT ?s WHERE {?s a <https://example.com/ontology#Person>}");
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            //System.out.println(qs.getResource("s"));
            list.add(qs.getResource("s").toString());
        }

        cnx.close();
        return list;
    }
        public List<String> getPlaces() {
        ArrayList<String> list=new ArrayList<String>();
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        QueryExecution qe = cnx.query("SELECT DISTINCT ?s WHERE {?s a <https://example.com/ontology#Place>}");
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            //System.out.println(qs.getResource("s"));
            list.add(qs.getResource("s").toString());
        }

        cnx.close();
        return list;
    }
    public void displayChiass(String selectedPicture, String selectedPerson)throws SempicModelException {

       
       System.out.println("Selected person: " + selectedPerson);
              System.out.println("Selected picture: " + selectedPicture);

       
        
    }
}

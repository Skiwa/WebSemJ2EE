/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.backingbeans;

import fr.uga.miashs.sempic.entities.SempicPicture;
import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_GSP;
import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_QUERY;
import static fr.uga.miashs.sempic.rdf.ExampleRDFConnection.ENDPOINT_UPDATE;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;

/**
 *
 * @author admin
 */
@Named
@RequestScoped
public class Research {
    
    private final static String ENDPOINT= "http://localhost:3030/sempic/";
    public final static String ENDPOINT_QUERY = ENDPOINT+"sparql"; // SPARQL endpoint
    public final static String ENDPOINT_UPDATE = ENDPOINT+"update"; // SPARQL UPDATE endpoint
    public final static String ENDPOINT_GSP = ENDPOINT+"data"; // Graph Store Protocol
    
    ArrayList<Resource> pictures = new ArrayList<Resource>();
    
    //Recherche photos avec au moins une personne
    public void searchBy1orMorePerson(){
        
        System.out.println("Request searchBy1orMorePerson");
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                        "WHERE {\n" +
                        "  ?picture a ex:Picture;\n" +
                        "           ex:subject ?s;\n" +
                        "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        
        cnx.close();
        
    }
    
    //Recherche photos avec aucune personne 
    public void searchWithNobody(){
        
       System.out.println("Request searchWithNobody");
       
       RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT ?picture\n" +
                "WHERE {\n" +
                "    ?picture a ex:Picture;\n" +
                "    FILTER NOT EXISTS{?picture ex:subject ?s}\n" +
            "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    
    //Recherche photos avec de la personne X
    public void searchWithX(){
        System.out.println("Request searchWithX");
        
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String nameProperty = request.getParameter("nameProperty");
        
        String name = nameProperty.substring(nameProperty.indexOf(" ")+1);
        String firstname = nameProperty.substring(0,nameProperty.indexOf(" "));
        
        //System.out.println(name);
        //System.out.println(firstname);
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
         String pref = ("PREFIX ex: <https://example.com/ontology#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                        "WHERE {\n" +
                        "  ?picture a ex:Picture;\n" +
                        "      ex:subject ?s.\n" +
                        "      ?s ex:firstName ?sFirstName. \n"+
                        "      ?s ex:lastName ?sLastName. \n"+
                        "      FILTER(?sFirstName = '"+firstname+"')"+
                        "      FILTER(?sLastName = '"+name+"')}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
        
    }
    
    //Recherche photos avec 2 personnes données
    //TODO : A changer plus tard pour ajouter X et Y (param)
    public void searchWithXandY(){
        System.out.println("Request searchWithXandY");
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT ?picture\n" +
                    "WHERE {\n" +
                    " ?picture a ex:Picture;\n" +
                    "     ex:subject ex:Manuel_Atencia;\n" +
                    "     ex:subject ex:Jerome_David.\n" +
                    "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
        
    }
    
    
    //Recherche photos avec des animaux
    public void searchWithAnimals(){
        System.out.println("Request searchWithAnimals");
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                        "WHERE {\n" +
                        " ?picture a ex:Picture;\n" +
                        "     ex:what ?animal.\n" +
                        "\n" +
                        "  ?animal a ?animalType.\n" +
                        "  ?animalType rdfs:subClassOf ex:Animal\n" +
                        "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
        
    }
    
    //Recherche photos avec l'animal de X (ici Manuel) 
    //TODO : A changer plus tard pour ajouter X (param)
    public void searchWithAnimalsofX(){
        System.out.println("Request searchWithAnimalsofX");
        
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String nameProperty = request.getParameter("nameUsersPet");
        
        String name = nameProperty.substring(nameProperty.indexOf(" ")+1);
        String firstname = nameProperty.substring(0,nameProperty.indexOf(" "));
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                    "WHERE {\n" +
                    "  ?person a ?personType.\n" +
                    "  ?personType rdfs:subClassOf ex:Person.\n" +
                    "  ?animal a ?animalType.\n" +
                    "  ?animalType rdfs:subClassOf ex:Animal.\n" +
                    "  ?person ex:pet ?animal.\n" +
                    "  ?picture a ex:Picture;\n" +
                    "           ex:what ?animal.\n" +
                    "   ?person ex:firstName ?personFirstName.\n" +
                    "   ?person ex:lastName ?personLastName.\n" +
                    "   FILTER(?personFirstName = '"+firstname+"')\n" +
                    "   FILTER(?personLastName = '"+name+"')"+
                    "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
        
    }
    
    //Recherche photos avec des enfants
    public void searchWithChilds(){
        System.out.println("Request searchWithChilds");
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                    "WHERE {\n" +
                    "  ?picture a ex:Picture;\n" +
                    "           ex:subject ?child.\n" +
                    "\n" +
                    "  ?child ex:birthday ?birthday.\n" +
                    "\n" +
                    "  FILTER (\n" +
                    "    ((((DAY(?birthday) - DAY(NOW())) * 24)) < 157680) && (YEAR(NOW())-YEAR(?birthday)<18)\n" +
                    "  )\n" +
                    "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
        
    }
    
    //Recherche photos avec seulement des adultes
    public void searchWithOnlyAdults(){
        System.out.println("Request searchWithOnlyAdults");
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                    "WHERE {\n" +
                    " ?picture a ex:Picture;\n" +
                    " ex:subject ?child.\n" +
                    " ?child ex:birthday ?birthday.\n" +
                    " FILTER (\n" +
                    " ((((DAY(?birthday) - DAY(NOW())) * 24)) < 157680) && (YEAR(NOW())-YEAR(?birthday)>=18)\n" +
                    " )\n" +
                    "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    
    //Recherche photos avec seulement des gens amis
    public void searchWithOnlyFriends(){
        System.out.println("Request searchWithOnlyFriends");
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                "WHERE {\n" +
                "  ?picture a ex:Picture;\n" +
                "      ex:subject ?person;\n" +
                "      ex:subject ?personFriend.\n" +
                "\n" +
                "  ?person a ?personType.\n" +
                "  ?personFriend a ?personType.\n" +
                "  ?personType rdfs:subClassOf ex:Person.\n" +
                "\n" +
                "  ?person ex:friend ?personFriend.\n" +
                "  ?personFriend ex:friend ?person.\n" +
                "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    
    //Recherche photos avec seulement des gens amis de X (ici Sadok)
    //TODO : Arranger pour ajouter une personne X
    public void searchWithOnlyFriendsofX(){
        System.out.println("Request searchWithOnlyFriendsofX");
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                        "WHERE {\n" +
                        "  ?picture a ex:Picture;\n" +
                        "      ex:subject ?person.\n" +
                        "\n" +
                        "  ?person a ?personType.\n" +
                        "  ?personType rdfs:subClassOf ex:Person.\n" +
                        "\n" +
                        "  ?person ex:friend ex:Sadok_Ben_Fredj.\n" +
                        "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    
    
    public ArrayList<Resource> getResult(){
        return pictures;
    }
    
}

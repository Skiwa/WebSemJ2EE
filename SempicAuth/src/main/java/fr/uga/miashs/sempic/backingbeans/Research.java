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
    
    //Recherche photos avec l'animal de X
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
                    "  ?child ex:birthday ?birthday.\n" +
                    "  FILTER (\n" +
                    "    ((((DAY(?birthday) - DAY(NOW())) * 24)) < 157680) && (YEAR(NOW())-YEAR(?birthday)<18)\n" +
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
                "  ?person a ?personType.\n" +
                "  ?personFriend a ?personType.\n" +
                "  ?personType rdfs:subClassOf ex:Person.\n" +
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
    
    //Recherche photos avec seulement des gens amis de X
    public void searchWithOnlyFriendsofX(){
        System.out.println("Request searchWithOnlyFriendsofX");
        
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String nameProperty = request.getParameter("nameUserFriend");
        
        String name = nameProperty.substring(nameProperty.indexOf(" ")+1);
        String firstname = nameProperty.substring(0,nameProperty.indexOf(" "));
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                        "WHERE {\n" +
                        "  ?picture a ex:Picture;\n" +
                        "      ex:subject ?person.\n" +
                        "  ?person a ?personType.\n" +
                        "  ?personType rdfs:subClassOf ex:Person.\n" +
                        "  ?person ex:friend ?ami .\n" +
                        "  ?ami ex:firstName ?amiFirstName.\n"+
                        "  ?ami ex:lastName ?amiLastName.\n"+
                        "   FILTER(?amiFirstName = '"+firstname+"')\n"+
                        "   FILTER(?amiLastName = '"+name+"')"+
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
    
    
    
    //Recherche photos prises dans un festival
    public void searchWhereFestival(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
       
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
            "WHERE {\n" +
            "  ?picture a ex:Picture;\n" +
            "           ex:event ?event.\n" +
            "\n" +
            "  ?event a ex:Festival\n" +
            "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    
    //Recherche photos prises dans un festival
    public void searchAtCulturalEvent(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
       
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
            "WHERE {\n" +
            "  ?picture a ex:Picture;\n" +
            "           ex:event ?event.\n" +
            "\n" +
            "  ?event a ?eventType.\n" +
            "  ?eventType rdfs:subClassOf ex:Cultural_Event.\n" +
            "}");
        
        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    
    
     //Recherche photos prises dans un festival avec un artiste qui joue
    public void searchWhereFestivalWithArtist(){
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String nameProperty = request.getParameter("artist");
        
        String name = nameProperty.substring(nameProperty.indexOf(" ")+1);
        String firstname = nameProperty.substring(0,nameProperty.indexOf(" "));
        
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
       
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
            "WHERE {\n" +
            "  ?picture a ex:Picture;\n" +
            "           ex:event ?event.\n" +
            "\n" +
            "  {?event a ex:Festival}UNION{?event a ex:Music_Event}\n" +
            "  ?event ex:featuredArtist ?artist." +
            "?artist ex:firstName ?artistFirstName.\n" +
            "?artist ex:lastName ?artistLastName.\n" +
            "FILTER(?artistFirstName = '"+firstname+"')\n" +
            "FILTER(?artistLastName = '"+name+"') "
                + "}");

        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    
    
    
    public void searchWithPeopleWorkingAtCompany(){
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String companyName = request.getParameter("company");
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
       
        QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
            "WHERE {\n" +
            "  ?picture a ex:Picture;\n" +
            "      ex:subject ?person.\n" +
            "\n" +
            "  ?person <https://example.com/ontology#job> ?job.\n" +
            "\n" +
            "  ?job ex:company ?company.\n"
                + "?company ex:companyName '" +companyName + "'}");

        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    
    
    

public void searchWhereCompanyLocation(){
        
        RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
        
        String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");
       
        QueryExecution qe = cnx.query(pref+"\n" +
            "SELECT DISTINCT ?picture\n" +
            "WHERE {\n" +
            "    ?picture a ex:Picture;\n" +
            "         ex:subject ?person;\n" +
            "         ex:subject ?colleague;\n" +
            "           ex:where ?officeLocation.\n" +
            "\n" +
            "      ?person <https://example.com/ontology#job> ?personJob.\n" +
            "     ?colleague <https://example.com/ontology#job> ?colleagueJob.\n" +
            "      ?personJob ex:colleague ?colleague.\n" +
            "\n" +
            "      ?personJob ex:company ?personCompany.\n" +
            "      ?personCompany ex:officeLocation ?officeLocation.\n" +
            "}\n" +
            "");

        ResultSet rs = qe.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            System.out.println(qs.getResource("?picture"));
            pictures.add(qs.getResource("?picture"));
        }
        cnx.close();
    }
    



    public void searchWhereColleaguesAtCompanyLocation(){

            RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);

            String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");

            QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                "WHERE {\n" +
                "   ?picture a ex:Picture;\n" +
                "            ex:where ?officeLocation.\n" +
                "\n" +
                "   ?company a ex:Company;\n" +
                "            ex:officeLocation ?officeLocation.\n" +
                "}");

            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                System.out.println(qs.getResource("?picture"));
                pictures.add(qs.getResource("?picture"));
            }
            cnx.close();
        }
    
    
     public void searchSelfies(){

            RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);

            String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");

            QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture (count( ?subject) as ?count)\n" +
                "WHERE {\n" +
                "  ?picture a ex:Picture;\n" +
                "           ex:subject ?subject.\n" +
                "}\n" +
                "GROUP BY ?picture");

            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                System.out.println(qs.getResource("?picture"));
                pictures.add(qs.getResource("?picture"));
            }
            cnx.close();
        }
        
        public void searchWhereCountry(){

            RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);

            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String country = request.getParameter("country");
            
            
            String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n");

            QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture WHERE {\n" +
                "  \n" +
                "  ?picture ex:where ?place;\n" +
                "           a ex:Picture.\n" +
                "  \n" +
                "  ?place a ?placeType.\n" +
                "  {?placeType rdfs:subClassOf ex:Place}UNION{?place a ex:Place}\n" +
                "  \n" +
                "  \n" +
                "  {     \n" +
                "    ?place ex:city ?city.\n" +
                "    ?city ex:region ?region.\n" +
                "    ?region ex:country ?country.\n" +
                "  }\n" +
                "  UNION\n" +
                "  {\n" +
                "    ?place ex:region ?region.\n" +
                "    ?region ex:country ?country.\n" +
                "  }\n" +
                "  UNION\n" +
                "  {\n" +
                "    ?place ex:country ?country \n" +
                "  }\n" +
                "  \n" +
                "  ?country ex:placeName '"+country+"'.\n" +
                "  \n" +
                "}");

            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                System.out.println(qs.getResource("?picture"));
                pictures.add(qs.getResource("?picture"));
            }
            cnx.close();
        }
        
        public void searchWhereCountrySpeaksLanguage(){

            RDFConnection cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);

            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String language = request.getParameter("language");
            
            String pref = ("PREFIX ex: <https://example.com/ontology#>\n"+
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\nPREFIX dbo: <http://dbpedia.org/ontology/>");

            QueryExecution qe = cnx.query(pref+"SELECT DISTINCT ?picture\n" +
                    "WHERE {\n" +
                    "	?picture a ex:Picture;\n" +
                    "	    ex:where ?place.\n" +
                    
                    "	?place a ?placeType.\n" +
                    "  	{?placeType rdfs:subClassOf ex:Place}UNION{?place a ex:Place}\n" +

                    "	{\n" +
                    "		?place ex:city ?city.\n" +
                    "		?city ex:region ?region.\n" +
                    "		?region ex:country ?country.\n" +
                    "	}UNION{\n" +
                    "    	?place ex:region ?region.\n" +
                    "    	?region ex:country ?country.\n" +
                    "	}UNION{\n" +
                    "    	#The place is a region\n" +
                    "    	?place ex:country ?country\n" +
                    "	}\n" +
                    "  \n" +
                    "  	?country ex:placeName ?countryName.\n" +
                    "  \n" +
                    "    SERVICE <http://dbpedia.org/sparql> {\n" +
                    "     ?dbCountry a <http://schema.org/Country>.\n" +
                    "     ?dbCountry rdfs:label ?dbCountryName.\n" +
                    "     FILTER(?dbCountryName = ?countryName).\n" +
                    "     \n" +
                    "     ?dbCountry dbo:language ?dbLanguage.\n" +
                    "     ?dbLanguage rdfs:label ?dbLanguageLabel.\n" +
                    "     FILTER(STR(?dbLanguageLabel) = '"+language+"')\n" +
                    "   }\n" +
                    "}");

            ResultSet rs = qe.execSelect();
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                System.out.println(qs.getResource("?picture"));
                pictures.add(qs.getResource("?picture"));
            }
            cnx.close();
        }
        
        
        
    
}
    
    
   





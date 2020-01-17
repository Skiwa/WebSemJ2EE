## Web Sem

---

- Toutes les photos d'une personne spécifique
  
  ```sql
  SELECT ?picture
  WHERE {
    ?picture a ex:Picture;
        ex:subject ex:Manuel_Atencia.
  }
  ```

- Toutes les photos avec des personnes
  
  ```sql
  SELECT ?picture
  WHERE {
    ?picture a ex:Picture;
             ex:subject ?s;
  }
  ```

- Toutes les photos sans personnes
  
  ```sql
  SELECT ?picture
  WHERE {
      ?picture a ex:Picture;
      FILTER NOT EXISTS{?picture ex:subject ?s}
  }
  ```

- Toutes les photos avec des gens qui sont amis
  
  ```sql
  PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
  SELECT DISTINCT ?picture
  WHERE {
    ?picture a ex:Picture;
        ex:subject ?person;
        ex:subject ?personFriend.
  
    ?person a ?personType.
    ?personFriend a ?personType.
    ?personType rdfs:subClassOf ex:Person.
  
    ?person <https://example.com/ontology#friend> ?personFriend.
    ?personFriend <https://example.com/ontology#friend> ?person.
  }
  ```

- Toutes les photos avec des gens qui sont amis d'une personne
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
    ?picture a ex:Picture;
        ex:subject ?person.
  
    ?person a ?personType.
    ?personType rdfs:subClassOf ex:Person.
  
    ?person <https://example.com/ontology#friend> ex:Sadok_Ben_Fredj.
  }
  ```

- Toutes les photos d'un évènement précis
  
  ```sql
  SELECT DISTINCT ?picture ?event
  WHERE {
    ?picture a ex:Picture;
        ex:event ex:TripBarcelone.
  }
  ```

- Toutes les photos d'un festival
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
    ?picture a ex:Picture;
             ex:event ?event.
  
    ?event a ex:Festival
  }
  ```

- Toutes les photos d'un evenement culturel
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
    ?picture a ex:Picture;
             ex:event ?event.
  
    ?event a ?eventType.
    ?eventType rdfs:subClassOf ex:Cultural_Event.
  }
  ```

- Toutes les photos d'un festival où un artiste particulier joue
  
  ```sql
  SELECT DISTINCT ?event
  WHERE {
    ?picture a ex:Picture;
             ex:event ?event.
  
    {?event a ex:Festival}UNION{?event a ex:Music_Event}
    ?event ex:featuredArtist ex:Bob_Dylan
  }
  ```

- Toutes les photos de personnes travaillant dans une entreprise particulière
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
    ?picture a ex:Picture;
        ex:subject ?person.
  
    ?person <https://example.com/ontology#job> ?job.
  
    ?job ex:company ex:UGA.
  }
  ```

- Toutes les photos avec des animaux
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
   ?picture a ex:Picture;
       ex:what ?animal.
  
    ?animal a ?animalType.
    ?animalType rdfs:subClassOf ex:Animal
  }
  ```

- Toutes les photos d'un animal de quelqu'un en particulier
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
    ?person a ?personType.
    ?personType rdfs:subClassOf ex:Person.
  
    ?animal a ?animalType.
    ?animalType rdfs:subClassOf ex:Animal.
  
    ?person ex:pet ?animal.
  
    ex:Manuel_Atencia ex:pet ?animal.
  
    ?picture a ex:Picture;
             ex:what ?animal.
  }
  ```

- Toutes les photos avec des enfants
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
    ?picture a ex:Picture;
             ex:subject ?child.
  
    ?child ex:birthday ?birthday.
  
    FILTER (
      ((((DAY(?birthday) - DAY(NOW())) * 24)) < 157680) && (YEAR(NOW())-YEAR(?birthday)<18)
    )
  }
  ```

- Toutes les photos avec des adultes uniquement
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
   ?picture a ex:Picture;
   ex:subject ?child.
   ?child ex:birthday ?birthday.
   FILTER (
   ((((DAY(?birthday) - DAY(NOW())) * 24)) < 157680) && (YEAR(NOW())-YEAR(?birthday)>=18)
   )
  }
  ```

- Toutes les photos prises dans une certaine periode de temps
  
  ```sql
  SELECT DISTINCT ?picture ?when
  WHERE {
    ?picture a ex:Picture;
             ex:when ?when.
  
    FILTER (?when > "2019-01-01T00:00:00"^^xsd:dateTime)
    FILTER (?when < "2019-12-31T23:59:59"^^xsd:dateTime)
  }
  ```

- Tous les selfies (avec d'autres personnes en plus)
  
  ```sql
  SELECT DISTINCT ?picture ?when
  WHERE {
    ?picture a ex:Picture;
             ex:subject ?subject;
             ex:creator ?subject;
  }
  ```

- Tous les selfies (une seule personne)
  
  ```sql
  SELECT DISTINCT ?picture (count( ?subject) as ?count)
  WHERE {
    ?picture a ex:Picture;
             ex:subject ?subject.
  }
  GROUP BY ?picture
  HAVING (?count = 1)
  ```


- Toutes les photos avec des gens qui sont collègues
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
     ?picture a ex:Picture;
              ex:subject ?person;
              ex:subject ?colleague.
  
     ?person <https://example.com/ontology#job> ?personJob.
     ?colleague <https://example.com/ontology#job> ?colleagueJob.
  
     ?personJob ex:colleague ?colleague.
  }
  ```

- Toutes les photos prises dans une entreprise
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
     ?picture a ex:Picture;
              ex:where ?officeLocation.
  
     ?company a ex:Company;
              ex:officeLocation ?officeLocation.
  }
  ```

- Toutes les photos de collègues prises dans leur entreprise
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
      ?picture a ex:Picture;
           ex:subject ?person;
           ex:subject ?colleague;
             ex:where ?officeLocation.
  
        ?person <https://example.com/ontology#job> ?personJob.
       ?colleague <https://example.com/ontology#job> ?colleagueJob.
        ?personJob ex:colleague ?colleague.
  
        ?personJob ex:company ?personCompany.
        ?personCompany ex:officeLocation ?officeLocation.
  }
  ```

- Toutes les photos prises dans un pays
  
  ```sql
  SELECT DISTINCT ?picture ?place ?region ?country
  WHERE {
    ?picture ex:where ?place.
    
    ?place a ?placeType.
    {?placeType rdfs:subClassOf ex:Place}UNION{?place a ex:Place}
    
    {
      #The place is located in a city
      ?place ex:city ?city.
      ?city ex:region ?region.
    	?region ex:country ?country.
    }UNION{
      #The place is located in a region (ex: city or geographical zone)
    	?place ex:region ?region.
      ?region ex:country ?country.
    }UNION{
      #The place is a region
    	?place ex:country ?country
    }
    
    FILTER (?country = ex:France)
  }
  ```

- Toutes les photos prises dans un pays où on parle une certaine langue
  
  ```sql
  SELECT DISTINCT ?picture
  WHERE {
  	picture a ex:Picture;
  	    ex:where ?place.
  	
  	?place a ?placeType.
    	{?placeType rdfs:subClassOf ex:Place}UNION{?place a ex:Place}
  
  	{
   		#The place is located in a city
  		?place ex:city ?city.
  		?city ex:region ?region.
  		?region ex:country ?country.
  	}UNION{
  		#The place is located in a region (ex: city or geographical zone)
      	?place ex:region ?region.
      	?region ex:country ?country.
  	}UNION{
      	#The place is a region
      	?place ex:country ?country
  	}
    
    	?country ex:placeName ?countryName.
    
      SERVICE <http://dbpedia.org/sparql> {
       #Retrieve the country
       ?dbCountry a <http://schema.org/Country>.
       ?dbCountry rdfs:label ?dbCountryName.
       FILTER(?dbCountryName = ?countryName).
       
       #Retrieve the language
       ?dbCountry dbo:language ?dbLanguage.
       ?dbLanguage rdfs:label ?dbLanguageLabel.
       FILTER(STR(?dbLanguageLabel) = "Français")
     }
  }
  ```



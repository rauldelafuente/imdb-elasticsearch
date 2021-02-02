# imdb-elasticsearch

Script which reads the content of an xlsx document with more than 50,000 IMDB films and create a JSON document to upload it later via Bulk API to Elasticsearch. The xls document also have te url of the film page in IMDB and we access it in order to be able to get more information.

## Requirements

Have the Excel document in the same folder as the project.

Also you need to create and index with the following mapping in Elastichsearch.

You canfind this script in mapping.sh

```bash
curl -X PUT "localhost:9200/films?pretty" -H 'Content-Type: application/json' -d'
{
    "mappings" : {
      "properties" : {
        "actors" : {
          "type" : "text",
          "fields" : {
            "actor" : {
              "type" : "keyword"
            }
          }
        },
        "genre" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword"
            }
          }
        },
        "imdbId" : {
          "type" : "long"
        },
        "sinopsis" : {
          "type" : "text"
        },
        "title" : {
          "type" : "text"
        },
        "year" : {
          "type" : "long"
        }
      }
    }
}
'
```


## Run

Go to the root directory and write the following command in bash.

You can find also a script called run.sh

```bash
mvn exec:java -Dexec.mainClass="testJsoup.Scraper" 
```

## Example of a row output of the JSON in Bulk format
```json
{"index":{"_id":"2559036"}}
{
  "imdbId":2559036,
  "title":"Olvidados ",
  "sinopsis":"After suffering a heart attack, retired General José Mendieta is haunted by his past as an officer in Operation Condor, the CIA-backed campaign of political repression in Latin America.",
  "year":2014,"genre":["Drama"],
  "actors":["Damián Alcázar","Rafael Ferro","Carla Ortiz","Tomás Fonzi","Ana Celentano","Eduardo Paxeco","Carloto Cotta","Guillermo Pfening","Shlomit Baytelman","Manuela Martelli","Cristian Mercado","Bernardo Peña","Claudia Lizaldi","Jorge Ortiz Sánchez","Lorenzo Quinteros"]
}
```


## Add information to Elasticsearch
Once you have the JSON written in Bulk format, you only have to write the following command in your bash (Obviously you need already an index in Elasticsearch).

Yuu can find this script in insert_documents.sh

```bash
curl -XPOST -H "Content-Type: application/json" "localhost:9200/films/_bulk?pretty" --data-binary "@resultBulk.json"
```

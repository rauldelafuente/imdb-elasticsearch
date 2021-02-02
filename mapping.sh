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

curl -XPOST -H "Content-Type: application/json" "localhost:9200/films/_bulk?pretty" --data-binary "@resultBulk.json"

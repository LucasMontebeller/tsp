(ns tsp.utils
  (:require [cheshire.core :as json]
            [tsp.graph :as graph]))

(defn read-json-file [file-path]
  "Lê o conteúdo do arquivo json."
  (json/parse-string (slurp file-path) true))

(defn build-node-map [data]
  "Constrói um mapa de nós a partir dos dados do JSON."
  (reduce
   (fn [acc node-data]
     (assoc acc
            (:ident node-data)
            (graph/add-neighbors (graph/create-node (:ident node-data))
                           (:neighbors node-data))))
   {}
   (:nodes data)))

;; Utilizar grafo direcionado ou não?
(defn read-graph-from-json [file-path]
  "Lê o arquivo JSON e retorna um grafo correspondente."
  (let [data (read-json-file file-path)
        nodes-map (build-node-map data)]
    (graph/->Graph (->> nodes-map (vals) (vec)))))
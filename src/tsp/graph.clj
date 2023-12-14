(ns tsp.graph
  (:require [cheshire.core :as json]))

(defrecord Node [ident neighbors])
(defrecord Graph [initial nodes])

(defn create-node [ident]
  "Cria um nó com o identificador especificado."
  (->Node ident #{}))

(defn add-neighbor [node neighbor distance]
  "Adiciona um vizinho ao nó e a respectiva distância que os separa."
  (update node :neighbors conj [neighbor distance]))

(defn add-neighbors [node neighbors-with-distances]
  "Adiciona uma coleção de vetores do tipo [neighbor distance] ao nó."
  (reduce (partial apply add-neighbor) node neighbors-with-distances))

(defn get-by-ident [nodes ident]
  (first (filter #(= (:ident %) ident) nodes)))

(defn get-neighbors [node]
  (get-in node [:neighbors]))

(defn visited? [node]
  (contains? node :visited))

(defn unvisited-neighbors [nodes node]
  (let [neighbors (get-neighbors node)]
    (filter
     (fn [n]
       (let [ident (first n)
             neighbor-node (get-by-ident nodes ident)]
         (not (visited? neighbor-node))))
     neighbors)))

(defn mark-visited [node]
  (assoc node :visited true))

(defn brute-force [graph])

;; Passar para outro arquivo?
(defn read-json-file [file-path]
  "Lê o conteúdo do arquivo json."
  (json/parse-string (slurp file-path) true))

(defn build-node-map [data]
  "Constrói um mapa de nós a partir dos dados do JSON."
  (reduce
   (fn [acc node-data]
     (assoc acc
            (:ident node-data)
            (add-neighbors (create-node (:ident node-data))
                           (:neighbors node-data))))
   {}
   (:nodes data)))

;; Utilizar grafo direcionado ou não?
(defn read-graph-from-json [file-path]
  "Lê o arquivo JSON e retorna um grafo correspondente."
  (let [data (read-json-file file-path)
        nodes-map (build-node-map data)
        initial-node (-> nodes-map vals first)]
    (->Graph initial-node (->> nodes-map (vals) (vec)))))
(ns tsp.graph
  (:require [cheshire.core :as json]))

(defrecord Node [ident neighbors])
(defrecord Graph [nodes])

(defn create-node [ident]
  "Cria um nó com o identificador especificado."
  (->Node ident #{}))

(defn add-neighbor [node neighbor distance]
  "Adiciona um vizinho ao nó e a respectiva distância que os separa."
  (update node :neighbors conj [neighbor distance]))

(defn add-neighbors [node neighbors-with-distances]
  "Adiciona uma coleção de vetores do tipo [neighbor distance] ao nó."
  (reduce (partial apply add-neighbor) node neighbors-with-distances))

(defn get-nodes [graph]
  "Retorna os nós do grafo."
  (get-in graph [:nodes]))

(defn get-nodes-idents [nodes]
  "Retorna todos os identifcadores do grafo."
  (->> nodes (map :ident) vec))

(defn get-by-ident [nodes ident]
  "Retorna um nó pelo seu identificador."
  (first (filter #(= (:ident %) ident) nodes)))

(defn get-neighbors [node]
  "Retorna os vizinhos pelo nó."
  (get-in node [:neighbors]))

(defn get-neighbors-by-ident [nodes node]
  "Retorna os vizinhos pelo identificador do nó."
  (get-neighbors (get-by-ident nodes node)))

(defn get-distance [neighbor]
  "Extrai a distância do vizinho."
  (when neighbor
    (->> neighbor
         (filter #(= :distance (first %)))
         first
         second)))

(defn neighbor-distance [nodes node1 node2]
  "Retorna a distância entre dois nós vizinhos no grafo, se houver uma conexão direta."
  (let [neighbors (get-neighbors-by-ident nodes node1)
        neighbor (first (filter #(= (first %) [:ident node2]) neighbors))
        distance (get-distance neighbor)]
    distance))

(defn connected? [nodes node1 node2]
  "Verifica se existe uma conexão direta entre dois nós no grafo."
  (let [neighbors (get-neighbors-by-ident nodes node1)]
    (some #(= (first %) [:ident node2]) neighbors)))

(defn valid-permutation? [nodes permutation]
  "Verifica se a permutação é válida no grafo, respeitando as conexões entre os nós."
  (every? (fn [[node1 node2]]
            (connected? nodes node1 node2))
          (partition 2 1 permutation)))

(defn valid-cycle? [nodes permutation]
  "Verifica se o último elemento da permutação consegue alcançar o primeiro nó grafo."
  (let [start-node (first permutation)
        end-node (last permutation)]
    (connected? nodes end-node start-node)))

(defn filter-valid-cycles [nodes all-permutations]
  "Filtra as permutações válidas que formam ciclos completos e inclui o primeiro elemento no final."
  (let [valid-cycles (filter #(valid-cycle? nodes %) all-permutations)
        cycles-with-start (map #(concat % [(first %)]) valid-cycles)]
    (mapv vec cycles-with-start)))

(defn valid-permutation-cycle? [nodes permutation]
  "Verifica se a permutação e o ciclo são válidos, adicionando o nó inicial no fim da permutação caso verdadeiro."
  (if (valid-permutation? nodes permutation)
    (let [valid-cycles (filter-valid-cycles nodes [permutation])]
      (when (seq valid-cycles)
        (first valid-cycles)))))

(defn total-distance [nodes route]
  "Calcula a distância total de uma determinada permutação de nós."
  (let [pairs (partition 2 1 route)
        distances (map #(apply neighbor-distance nodes %) pairs)
        total-distance (apply + distances)]
    total-distance))

(defn calculate-all-distances [nodes all-permutations]
  "Calcula a distância total para cada caminho na lista de todas as permutações."
  (map #(total-distance nodes %) all-permutations))

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
        nodes-map (build-node-map data)]
    (->Graph (->> nodes-map (vals) (vec)))))
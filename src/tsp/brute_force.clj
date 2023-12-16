(ns tsp.brute_force
  (:require [tsp.graph :as graph]))

(defn valid-permutation? [nodes permutation]
  "Verifica se a permutação é válida no grafo, respeitando as conexões entre os nós."
  (every? (fn [[node1 node2]]
            (graph/connected? nodes node1 node2))
          (partition 2 1 permutation)))

(defn permute [idents nodes]
  "Calcula todas as permutações possíveis entre os nós do grafo."
  (if (<= (count idents) 1)
    [idents]
    (for [x idents
          ys (permute (remove #{x} idents) nodes)
          :when (valid-permutation? nodes (conj ys x))]
      (conj ys x))))

(defn total-distance [nodes route]
  "Calcula a distância total de uma determinada permutação de nós."
  (let [pairs (partition 2 1 route)
        distances (map #(apply graph/neighbor-distance nodes %) pairs)
        total-distance (apply + distances)]
    total-distance))

(defn calculate-all-distances [nodes all-permutations]
  "Calcula a distância total para cada caminho na lista de todas as permutações."
  (map #(total-distance nodes %) all-permutations))

(defn min-permutation-distance [permutation-distances]
  "Encontra a menor distância e retorna a permutação associada."
  (let [min-distance (apply min (vals permutation-distances))
        min-permutation (some (fn [[permutation distance]]
                                (when (= distance min-distance)
                                  permutation))
                              permutation-distances)]
    {:permutation min-permutation
     :distance min-distance}))

(defn exec [graph]
  "Resolve o problema do caixeiro viajante por força bruta."
  (let [nodes (graph/get-nodes graph)
        nodes-idents (->> nodes (map :ident) vec)
        all_permutations (permute nodes-idents nodes)
        all_distances (calculate-all-distances nodes all_permutations)
        permutation_distances (zipmap all_permutations all_distances)
        min_permutation_distance (min-permutation-distance permutation_distances)]
    {:all-distances permutation_distances
     :min-distance min_permutation_distance}))
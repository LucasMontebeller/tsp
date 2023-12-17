(ns tsp.brute_force
  (:require [tsp.graph :as graph]))

(defn permute [idents nodes]
  "Calcula todas as permutações possíveis entre os nós do grafo."
  (if (<= (count idents) 1)
    [idents]
    (for [x idents
          ys (permute (remove #{x} idents) nodes)
          :when (graph/valid-permutation? nodes (conj ys x))]
      (conj ys x))))

(defn min-permutation-distance [permutation-distances]
  "Encontra a menor distância e retorna a permutação associada."
  (let [min-distance (apply min (vals permutation-distances))
        min-permutation (some (fn [[permutation distance]]
                                (when (= distance min-distance)
                                  permutation))
                              permutation-distances)]
    {:best-route min-permutation
     :cost min-distance}))

(defn exec [graph]
  "Resolve o problema do caixeiro viajante por força bruta."
  (let [nodes (graph/get-nodes graph)
        nodes-idents (graph/get-nodes-idents nodes)
        all_permutations (permute nodes-idents nodes)
        valid_cycles_permutations (graph/filter-valid-cycles nodes all_permutations)
        all_distances (graph/calculate-all-distances nodes valid_cycles_permutations)
        permutation_distances (zipmap valid_cycles_permutations all_distances)
        min_permutation_distance (min-permutation-distance permutation_distances)]
    {:all-distances permutation_distances
     :min-distance min_permutation_distance}))
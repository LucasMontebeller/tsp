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

(defn exec [graph]
  "Resolve o problema do caixeiro viajante por força bruta."
  (let [nodes (graph/get-nodes graph)
        nodes-idents (->> nodes (map :ident) vec)
        all_permutations (permute nodes-idents nodes)]
    all_permutations))
(ns tsp.simulated_annealing
  (:require [tsp.graph :as graph]))

(defn random-solution [idents nodes]
  "Embaralha uma lista de identificadores do nó, até que seja encontrada uma solução valida."
  (loop [shuffled-nodes (shuffle idents)]
    (if-let [valid-cycle (graph/valid-permutation-cycle? nodes shuffled-nodes)]
      valid-cycle
      (recur idents))))

(defn generate-neighbor [idents nodes]
  "Gera um vizinho aleatório."
  (random-solution idents nodes))

(defn accept-neighbor? [energy temperature]
  "Define se será aceito ou não um novo vizinho pelo fator de Boltzmann."
  (let [boltzmann-factor (Math/exp (- (/ energy temperature)))]
    (if (< (rand) boltzmann-factor) true false)))

(defn exec [graph]
  (let [initial-temperature 1000 ;; verificar se virá como parametro
        cooling-rate 0.99
        nodes (graph/get-nodes graph)
        nodes-idents (graph/get-nodes-idents nodes)
        current-solution (random-solution nodes-idents nodes) ;; sol inicial
        best-solution current-solution
        best-cost (graph/total-distance nodes best-solution)
        temperature initial-temperature]
    (loop [best-solution best-solution
           best-cost best-cost
           current-solution current-solution
           temperature temperature
           iter 0]
      (if (>= iter 10000)
        {:best-route best-solution
         :cost best-cost}
        (let [current-solution-cost (graph/total-distance nodes current-solution)
              neighbor (generate-neighbor (butlast current-solution) nodes)
              neighbor-cost (graph/total-distance nodes neighbor)
              delta-e (- neighbor-cost current-solution-cost)
              new-current-solution (if (or (< delta-e 0) (accept-neighbor? delta-e temperature))
                                     neighbor
                                     current-solution)
              new-current-solution-cost (graph/total-distance nodes new-current-solution)
              new-best-solution (if (< new-current-solution-cost best-cost) ;; atualiza o melhor estado
                              new-current-solution
                              best-solution)
              new-best-cost (if (< new-current-solution-cost best-cost) ;; atualiza o melhor custo
                              new-current-solution-cost
                              best-cost)]
          (recur new-best-solution new-best-cost new-current-solution (* temperature cooling-rate) (inc iter)))))))
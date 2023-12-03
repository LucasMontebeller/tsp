(ns tsp.graph)

(defrecord Node [ident neighbors])
(defrecord Graph [initial nodes])

(defn create-node [ident]
  "Cria um nó com o identificador especificado."
  (->Node ident #{}))

(defn add-neighbor [node neighbor distance]
  "Adiciona um vizinho ao nó e a respectiva distância que os separa."
  (update node :neighbors conj [neighbor :distance distance]))

(defn add-neighbors [node neighbors-with-distances]
  "Adiciona uma coleção de vetores do tipo [neighbor distance] ao nó."
  (reduce (partial apply add-neighbor) node neighbors-with-distances))

;; Utilizar grafo direcionado ou não?
(defn gen-graph []
  (let [nodeA (create-node :A)
        nodeB (create-node :B)
        nodeC (create-node :C)
        nodeD (create-node :D)
        nodeE (create-node :E)
        nodeF (create-node :F)]
    (-> nodeA
        (add-neighbors [[nodeB 2] [nodeC 5] [nodeD 4]]))
    (-> nodeB
        (add-neighbors [[nodeC 3] [nodeD 7] [nodeE 1]]))
    (-> nodeC
        (add-neighbors [[nodeD 6] [nodeE 8] [nodeF 9]]))
    (-> nodeD
        (add-neighbors [[nodeE 5] [nodeF 3]]))
    (-> nodeE
        (add-neighbors [[nodeF 6]]))
    (-> nodeF
        (add-neighbors [[nodeA 2]]))
    (->Graph nodeA [nodeA nodeB nodeC nodeD nodeE nodeF])))
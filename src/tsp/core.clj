(ns tsp.core
  (:require [tsp.graph :as graph]
            [tsp.brute_force :as brute_force]
            [clojure.java.shell :refer [sh]])
  (:gen-class))

(defn exec-py-script [file-path]
  (let [output (sh "python3" "show_graph.py" file-path)]
    (println (:out output))
    (println (:err output))))

(defn -main
  [& args]
  (let [file-path (if (empty? args)
                    "tsp-sources/graph1.json"
                    (first args)) 
        graph (graph/read-graph-from-json file-path)
        possible-paths (tsp.brute_force/exec graph)]
    (do 
      (exec-py-script file-path) 
      (println graph) 
      (println possible-paths))))
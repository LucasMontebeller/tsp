(ns tsp.core
  (:require [tsp.graph :as graph] 
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
                    (first args))]
    (exec-py-script file-path)
    (println (graph/read-graph-from-json file-path))))
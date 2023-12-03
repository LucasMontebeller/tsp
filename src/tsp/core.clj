(ns tsp.core
  (:require [tsp.graph :as graph])
  (:gen-class))

(defn -main
  [& args]
  (println (graph/gen-graph)))
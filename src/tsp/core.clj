(ns tsp.core
   (:require [tsp.graph :as graph]
             [tsp.utils :as utils]
             [tsp.brute_force :as brute_force]
             [tsp.simulated_annealing :as simulated_annealing]
             [clojure.java.shell :refer [sh]]
             [clojure.string :as string])
   (:gen-class))

 (defn exec-py-script [file-path]
   (let [output (sh "python3" "show_graph.py" file-path)]
     (println "Nova visualização disponível em tsp-sources/images/graph1.png")
     (println (:out output))
     (println (:err output))))
 
 (defn print-separator []
   (do
     (println)
     (println "--------------------------------------------------------------------------------------------------------------------------")
     (println)))

 (defn -main [& args]
   (let [file-path (if (empty? args)
                     "tsp-sources/graph1.json"
                     (first args))
         graph (utils/read-graph-from-json file-path)]
     (println "Escolha o algoritmo para calcular o caminho:")
     (println "1. Força Bruta")
     (println "2. Simulated annealing")
     (flush)
     (let [choice (read-line)]
       (cond
         (= choice "1") (let [start-time (System/currentTimeMillis)
                              brute-force-result (tsp.brute_force/exec graph)
                              end-time (System/currentTimeMillis)]
                          (print-separator)
                          (println "Caminhos calculados:\n" (get-in brute-force-result [:all-distances]))
                          (println)
                          (println "Resultado:" (get-in brute-force-result [:min-distance]))
                          (println "Tempo de execução (ms):" (- end-time start-time))
                          (print-separator))
         (= choice "2") (let [start-time (System/currentTimeMillis)
                              simulated-annealing-result (tsp.simulated_annealing/exec graph)
                              end-time (System/currentTimeMillis)]
                          (print-separator)
                          (println "Resultado:" simulated-annealing-result)
                          (println "Tempo de execução (ms):" (- end-time start-time))
                          (print-separator))
         :else (println "Escolha inválida. Por favor, insira 1 ou 2 para selecionar o algoritmo.")))
     (print "Deseja gerar uma visualização do grafo? (S/N): ")
     (flush)
     (let [choice (string/lower-case (read-line))]
       (cond
         (= choice "s") (exec-py-script file-path)
         (= choice "n") (println "Programa encerrado.")
         :else (println "Opção inválida. Não será gerada a visualização do grafo.")))) 
         (System/exit 0))
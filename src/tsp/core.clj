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
     (println (:out output))
     (println (:err output))))
 
 (defn print-separator []
   (do
     (println)
     (println "--------------------------------------------------------------------------------------------------------------------------")
     (println)))
 
(defn get-simulated-annealing-params []
  (println "Insira os parâmetros para Simulated Annealing:")
  (print "Temperatura inicial (padrão: 100): ")
  (flush)
  (let [initial-temp (read-line)
        initial-temp-float (if (empty? initial-temp) 100 (Float/parseFloat initial-temp))]
    (print "Taxa de resfriamento (padrão: 0.975): ")
    (flush)
    (let [cooling-rate (read-line)
          cooling-rate-float (if (empty? cooling-rate) 0.975 (Float/parseFloat cooling-rate))]
      (print "Número máximo de iterações (padrão: 1000): ")
      (flush)
      (let [max-iterations (read-line)
            max-iterations-int (if (empty? max-iterations) 1000 (Integer/parseInt max-iterations))]
        (print "Número máximo de iterações em um mínimo local ou global (padrão: 30): ")
        (flush)
        (let [best-limit-stop (read-line)
              best-limit-stop (if (empty? best-limit-stop) 30 (Integer/parseInt best-limit-stop))] 
          (println)
        {:initial-temp initial-temp-float
         :cooling-rate cooling-rate-float
         :max-iterations max-iterations-int
         :best-limit-stop best-limit-stop})))))

(defn -main [& args]
  (let [file-path (if (empty? args)
                    (do
                      (println "Insira o caminho do arquivo JSON:")
                      (let [input-path (read-line)]
                        (if (empty? input-path)
                          "tsp-sources/graph1.json"
                          input-path)))
                    (first args)) 
        graph (utils/read-graph-from-json file-path)] 
    (println "Escolha o algoritmo:") 
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
        (= choice "2") (let [simulated-annealing-params (get-simulated-annealing-params)
                             initial-temp (:initial-temp simulated-annealing-params)
                             cooling-rate (:cooling-rate simulated-annealing-params)
                             max-iterations (:max-iterations simulated-annealing-params)
                             best-limit-stop (:best-limit-stop simulated-annealing-params)
                             start-time (System/currentTimeMillis) 
                             simulated-annealing-result (tsp.simulated_annealing/exec graph initial-temp cooling-rate max-iterations best-limit-stop) 
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
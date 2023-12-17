# tsp

Este projeto implementa algoritmos para resolver o Problema do Caixeiro Viajante (TSP) em Clojure. 

## Installation

Para executar o código deste projeto, é necessário ter o Leiningen instalado. 
Para isso acesse https://leiningen.org/.

## Usage
O programa aceita um arquivo JSON como entrada, contendo informações sobre o grafo a ser analisado, como o exemplo abaixo.

```json
{
  "nodes": [
    {
      "ident": "A",
      "neighbors": [
        { "ident": "B", "distance": 3 },
        { "ident": "C", "distance": 2 },
        { "ident": "D", "distance": 4 },
        { "ident": "E", "distance": 5 }
      ]
    },
    {
      "ident": "B",
      "neighbors": [
        { "ident": "A", "distance": 3 },
        { "ident": "C", "distance": 1 },
        { "ident": "D", "distance": 2 },
        { "ident": "E", "distance": 3 }
      ]
    },
    {
      "ident": "C",
      "neighbors": [
        { "ident": "A", "distance": 2 },
        { "ident": "B", "distance": 1 },
        { "ident": "D", "distance": 3 },
        { "ident": "E", "distance": 2 }
      ]
    },
    {
      "ident": "D",
      "neighbors": [
        { "ident": "A", "distance": 4 },
        { "ident": "B", "distance": 2 },
        { "ident": "C", "distance": 3 },
        { "ident": "E", "distance": 1 }
      ]
    },
    {
      "ident": "E",
      "neighbors": [
        { "ident": "A", "distance": 5 },
        { "ident": "B", "distance": 3 },
        { "ident": "C", "distance": 2 },
        { "ident": "D", "distance": 1 }
      ]
    }
  ]
}
```

### Para executar via REPL:
    lein run

### Para executar via arquivo .jar:
#### 1. Crie o arquivo JAR:
    lein uberjar

##### 2. Navegar até o diretório com o arquivo JAR:
    cd target/uberjar

##### 3. Executar o programa:
    java -jar tsp-0.1.0-SNAPSHOT-standalone.jar

Em ambos os casos irá abrir um prompt solicitando dados para a execução do programa, 
sendo o primeiro deles o caminho do arquivo json solicitado. Caso não seja informado, irá usar como *default* o arquivo graph1.json dentro do diretório ```tsp-sources```.

Ao final de cada execução também foi incluido uma visualização opcional do grafo, gerada através do arquivo python ```show_graph.py``` e salva como imagem no diretório ```tsp-sources```.
## Options

FIXME: listing of options this app accepts.

## Examples

...
### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2023 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

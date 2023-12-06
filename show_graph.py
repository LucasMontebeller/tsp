import sys
import json
import networkx as nx
import matplotlib.pyplot as plt
import os

def read_graph_from_json(file_path):
    with open(file_path) as f:
        data = json.load(f)
        G = nx.DiGraph()

        for node_data in data['nodes']:
            G.add_node(node_data['ident'])
            for neighbor in node_data['neighbors']:
                G.add_edge(node_data['ident'], neighbor['ident'], weight=neighbor['distance'])

        return G

if __name__ == '__main__':
    try:
        if len(sys.argv) < 2:
            sys.exit(1)

        file_path = sys.argv[1]
        if not os.path.exists(file_path):
            raise Exception(f'Arquivo {file_path} não encontrado.')
        
        graph = read_graph_from_json(file_path)
        pos = nx.spring_layout(graph)
        labels = {node: node for node in graph.nodes()}

        plt.figure(figsize=(8, 6))
        nx.draw_networkx_nodes(graph, pos, node_size=800, node_color='skyblue')
        nx.draw_networkx_edges(graph, pos, arrows=True)
        nx.draw_networkx_labels(graph, pos, labels=labels, font_weight='bold')
        edge_labels = nx.get_edge_attributes(graph, 'weight')
        nx.draw_networkx_edge_labels(graph, pos, edge_labels=edge_labels)

        plt.show()
    except Exception as ex:
        print(f'Falha ao gerar visualização do grafo: {ex}')
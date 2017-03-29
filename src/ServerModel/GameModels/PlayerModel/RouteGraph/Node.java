package ServerModel.GameModels.PlayerModel.RouteGraph;

import java.util.List;

/**
 * Created by benjamin on 28/03/17.
 *  Each node represents a city.
 */
public class Node {

    public Node(String name){
        _nodeName = name;
    }

    private String _nodeName;
    private List<Edge> _edges;

    //--------------------------------------MODIFIERS---------------------------------------//
    public void addEdge(Edge e){
        _edges.add(e);
    }

    //--------------------------------------GETTERS AND SETTERS-----------------------------//
    public String get_nodeName() { return _nodeName; }
    public void set_nodeName(String _nodeName) { this._nodeName = _nodeName; }

    public List<Edge> get_edges() { return _edges; }
    public void set_edges(List<Edge> _edges) { this._edges = _edges; }

    //--------------------------------------CLASS FUNCTIONS---------------------------------//
    public Boolean addEdge(int weight, Node nodePointer){
        Edge newEdge = new Edge(weight, nodePointer);
        if(_edges.add(newEdge)){
            return true;
        }
        return false;
    }
}
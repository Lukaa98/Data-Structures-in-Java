
import java.util.ArrayList;


interface GraphADT{
    Vertex insertVertex(String n);// insert a new vertex with name n – return the new vertex
    String removeVertex(Vertex v); // remove the vertex v – return the vertex name
    void insertEdge(Vertex v, Vertex w, String n); //Insert new edge with endpoints v and w and name n
    String removeEdge(Edge e); //remove the edge e - return the edge name
    Vertex opposite(Edge e, Vertex v); // Return endpoint of edge e that is opposite endpoint v
    ArrayList<Vertex> vertices(); //return an iterable collection of all the graph vertices
    ArrayList<Edge> edges(); //return an iterable collection of all the graph edges
    boolean areAdjacent(Vertex v, Vertex w); //return true if v, w are adjacent; false otherwise
    ArrayList<Edge> incidentEdges(Vertex v); //return an iterable collection of the edges incident to v
    String rename(Vertex v, String n); //rename existing vertex v as n; return old vertex name
    String rename(Edge e, String n); //rename existing edge e as n; return old edge name
}
public class Graph implements GraphADT{
    private ArrayList<Vertex> vertices;
    private ArrayList<Edge> edges;

    public Graph (ArrayList<Vertex> vertices, ArrayList<Edge> edges){
        this.vertices = vertices;
        this.edges = edges;
    }

    @Override
    public Vertex insertVertex(String n) {
        Vertex v = new Vertex((n));
        this.vertices.add(v);
        return v;
    }

    /**
     *
     * @param v
     * @return
     */
    @Override
    public String removeVertex(Vertex v) {
        String retVal = null;

        //before removing vertex, remove edges connecting this vertex to others
        ArrayList<Edge> edges = this.incidentEdges(v);
        for (Edge edge : edges){
            this.edges.remove(edge);
        }

        //now, remove the edge if it exists
        if (this.vertices.indexOf(v) != -1){
            retVal = v.getName();
            this.vertices.remove(v);
        }

        //return vertex name or null if not found
        return retVal;
    }

    @Override
    public void insertEdge(Vertex v, Vertex w, String n) {
        Edge edge = new Edge(n, v, w);
        this.edges.add(edge);
    }

    @Override
    public String removeEdge(Edge e) {
        String  retVal;
        //if such edge exists, remove it
        if (this.edges.indexOf(e) != -1){
            retVal = e.getName();
            this.edges.remove(e);
            return retVal;
        }
        //no such edge is found
        return null;
    }

    @Override
    public Vertex opposite(Edge e, Vertex v) {
        int index;
        if (e != null){
            ArrayList<Vertex> endpoints = e.getEndpoints();
            if (endpoints.indexOf(v) == -1){
                return null;
            }
            index = endpoints.indexOf(v);
            return endpoints.get(index == 0? 1: 0);
        }
        return null;
    }

    @Override
    public ArrayList<Vertex> vertices() {
        return this.vertices;
    }

    @Override
    public ArrayList<Edge> edges() {
        return this.edges;
    }

    @Override
    public boolean areAdjacent(Vertex v, Vertex w) {
        for (Edge e : this.edges){
            ArrayList<Vertex> endpoints = e.getEndpoints();
            if ((endpoints.indexOf(v) != -1) && (endpoints.indexOf(w) != -1)){
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Edge> incidentEdges(Vertex v) {
        ArrayList<Edge> incEdges = new ArrayList<>();
        for (Edge edge : this.edges){
            if (edge.getEndpoints().indexOf(v) != -1){
                incEdges.add(edge);
            }
        }
        return incEdges;
    }

    @Override
    public String rename(Vertex v, String n) {
        String oldName = v.getName();
        v.setName(n);
        return oldName;
    }

    @Override
    public String rename(Edge e, String n) {
        String oldName = e.getName();
        e.setName(n);
        return oldName;
    }


    //traverse graph starting from vertex v
    public void bftraverse(Vertex v){
        //keep track of visited vertices
        ArrayList<Vertex> visited = new ArrayList<>();
        //fifo will be used to traverse graph in breath first search manner
        ArrayList<Vertex> fifo = new ArrayList<>();
        //put the starting vertex in the fifo and to visited
        fifo.add(v);
        visited.add(v);

        //repeat until the fifo is empty
        while (!fifo.isEmpty()){
            //take one vertex from a fifo (removing element 0 is equivalent to that)
            Vertex vertex = (Vertex) fifo.remove(0);
            //print a message about visited vertex
            System.out.println("I have visited vertex " + vertex.getName());
            //find all incident vertices from that one
            ArrayList<Edge> incidentEdges = incidentEdges(vertex);
            for (Edge e : incidentEdges){
                //and put all of them not visited before in the fifo so they
                //will be traversed in next iterations
                if (visited.indexOf(opposite(e, vertex)) == -1){
                    fifo.add(opposite(e, vertex));
                    visited.add(opposite(e, vertex));
                }
            }
        }
    }

    //traverse complete graph even if it is not connected
    public void bftraverse(){
        //keep track of visited vertices
        ArrayList<Vertex> visited = new ArrayList<>();

        ArrayList<Vertex> verticesCopy = new ArrayList<>();
        //make a copy of vertices because we will remove the ones that are
        //traversed and we cannot do that on original collection
        for (Vertex v : this.vertices){
            verticesCopy.add(v);
        }

        //repeat until all vertices are visited
        while (!verticesCopy.isEmpty()){
            ArrayList<Vertex> fifo = new ArrayList<>();
            //remove the first one, or first from remaining in next iterations
            Vertex v = verticesCopy.remove(0);
            //put it in the fifo in order to find all reachable from that one
            if (visited.indexOf(v) == -1){
                fifo.add(v);
                visited.add(v);
            }
            while(!fifo.isEmpty()){
                Vertex vertex = (Vertex) fifo.remove(0);
                //visit all incident edges from vertex
                ArrayList<Edge> incidentEdges = incidentEdges(vertex);
                //similar to before, put all of them not visited in the fifo
                for (Edge e : incidentEdges) {
                    if (visited.indexOf(opposite(e, vertex)) == -1) {
                        fifo.add(opposite(e, vertex));
                        visited.add(opposite(e, vertex));
                    }
                }
                //remove each traversed vertex from a collection
                if (verticesCopy.indexOf(vertex) != -1){
                    verticesCopy.remove(vertex);
                }
                System.out.println("I have visited vertex " + vertex.getName());
            }
        }
    }

    // return a list (ArrayList) of all of the stations (vertices) that can be reached by rail when starting from v.
    public ArrayList<Vertex> allReachable(Vertex v){
        //keep track of visited vertices
        ArrayList<Vertex> visited = new ArrayList<>();
        //create arraylist that will be returned
        ArrayList<Vertex> retArray = new ArrayList<>();
        //prepare fifo for bfs traversal
        ArrayList<Vertex> fifo = new ArrayList<>();
        //put v on the fifo and to visited
        fifo.add(v);
        visited.add(v);

        while (!fifo.isEmpty()){
            //get a vertex from a fifo
            Vertex vertex = (Vertex) fifo.remove(0);
            //find all incident vertices
            ArrayList<Edge> incidentEdges = incidentEdges(vertex);
            //place all non-visited incident vertices on the fifo so each of them will be traversed in
            //the next while iteration
            for (Edge e : incidentEdges) {
                if (visited.indexOf(opposite(e, vertex)) == -1) {
                    fifo.add(opposite(e, vertex));
                    visited.add(opposite(e, vertex));
                }
            }
            //make sure that starting vertex is not placed in arraylist that is returned
            if (!vertex.equals(v)){
                retArray.add(vertex);
            }
        }
        return retArray;
    }

    // return true if all the stations in the entire rail network can be reached from one another, and otherwise, return false.
    public boolean allConnected(){
        ArrayList<Vertex> verticesCopy = new ArrayList<>();
        //make a copy of vertices since we will modify this and we cannot do that on the origina collection
        for (Vertex v : this.vertices){
            verticesCopy.add(v);
        }

        //take one from vertices randomly
        Vertex v = verticesCopy.remove(0);

        //get all vertices reachable from this one
        ArrayList<Vertex> reachableVertices = allReachable(v);

        //check if all are reachable from v
        //do that by removing all reachable from collection copy
        for (Vertex reachableVertex : reachableVertices){
            verticesCopy.remove(reachableVertex);
        }
        //not all reachable if any vertex remains in a copy collection
        return verticesCopy.isEmpty();
    }

    @Override
    public String toString(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Graph vertices: \n");
        for (Vertex vertex : this.vertices){
            strBuilder.append(vertex).append("\n");
        }
        strBuilder.append("With connected edges: \n");
        for (Edge edge : this.edges){
            strBuilder.append(edge.getEndpoints().get(0)).append(" and ").append(edge.getEndpoints().get(1)).append("\n");
        }
        return strBuilder.toString();
    }

    public static void main(String[] args){
        ArrayList<Vertex> vertices = new ArrayList<>();
        vertices.add(new Vertex("Waterloo"));//       0   A
        vertices.add(new Vertex("Paddington"));//     1   B
        vertices.add(new Vertex("Kings cross"));//    2   C
        vertices.add(new Vertex("St Pancras"));//     3   D
        vertices.add(new Vertex("Euston"));//         4   E
        vertices.add(new Vertex("Charing cross"));//  5   F
        vertices.add(new Vertex("Victoria"));//       6   G
        vertices.add(new Vertex("London Bridge"));//  7   H
        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("AB", vertices.get(0), vertices.get(1)));
        edges.add(new Edge("AC", vertices.get(0), vertices.get(2)));
        edges.add(new Edge("AF", vertices.get(0), vertices.get(5)));
        edges.add(new Edge("BH", vertices.get(1), vertices.get(7)));
        edges.add(new Edge("CD", vertices.get(2), vertices.get(3)));
        edges.add(new Edge("BE", vertices.get(1), vertices.get(6)));
        edges.add(new Edge("GC", vertices.get(6), vertices.get(2)));

        Graph graph = new Graph(vertices, edges);
        System.out.println(graph);
        System.out.println("Added " + graph.insertVertex("Brighton") + " to graph.");
        System.out.println(graph);
        String vertexName = graph.removeVertex(graph.vertices().get(5));
        if (vertexName != null)
            System.out.println("Removed vertex " + vertexName);
        System.out.println(graph);
        System.out.println("Adding vertex connecting Victoria and Euston");
        graph.insertEdge(graph.vertices().get(5), graph.vertices().get(4), "GE");
        System.out.println(graph);
        String edgeName = graph.removeEdge(graph.edges().get(1));
        if (edgeName != null)
            System.out.println("Removing edge " + edgeName + " (Charing Cross)");
        System.out.println(graph);
        Edge edge = graph.edges().get(3);
        System.out.println("On " + edge + " opposite from " + edge.getEndpoints().get(0) + " is " + graph.opposite(edge, edge.getEndpoints().get(0)));
        Vertex v1 = graph.vertices().get(3);
        Vertex v2 = graph.vertices().get(5);
        String adjacent = graph.areAdjacent(v1, v2) ? " are adjacent " : " are not adjacent ";
        System.out.println(v1 + " and " + v2 + adjacent);

        v1 = graph.vertices().get(1);
        v2 = graph.vertices().get(6);
        adjacent = graph.areAdjacent(v1, v2) ? " are adjacent " : " are not adjacent ";
        System.out.println(v1 + " and " + v2 + adjacent);

        System.out.println("Vertex " + graph.rename(graph.vertices().get(4), "Falmer") + " is renamed to " + graph.vertices().get(4).getName());
        System.out.println(graph);
        System.out.println("Edge " + graph.rename(graph.edges().get(5), "GW") + " is renamed to " + graph.edges().get(5).getName());
        System.out.println(graph);

        graph.bftraverse(graph.vertices().get(1));
        String connected = graph.allConnected()? "connected" : "not connected";
        System.out.println("Graph is " + connected);
        graph.bftraverse();

        System.out.println("Inserting edge between Waterloo and Brighton");
        graph.insertEdge(graph.vertices().get(0), graph.vertices().get(graph.vertices().size()-1), "AJ");
        System.out.println(graph);
        connected = graph.allConnected()? "connected" : "not connected";
        System.out.println("Graph is " + connected);


    }
}

class Vertex{
    private String name;
    public Vertex(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //similar to Graph class, this is added to print Vertex class references
    @Override
    public String toString(){
        return "Vertex " + this.getName();
    }
}

class Edge{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    ArrayList<Vertex> endpoints;

    public ArrayList<Vertex> getEndpoints() {
        return endpoints;
    }
    public Edge (String name, Vertex endpoint1, Vertex endpoint2){
        this.name = name;
        this.endpoints = new ArrayList<>();
        this.endpoints.add(endpoint1);
        this.endpoints.add(endpoint2);
    }

    //similar to Graph class, this is added to print Edge class references
    @Override
    public String toString(){
        return "Edge " + this.getName() + ", connecting " + this.endpoints.get(0) + " and " + this.endpoints.get(1);
    }
}

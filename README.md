model a rail transport network as an undirected graph, where the graph vertices represent 
the stations and edges represent direct rail links between them. You will need to write a 
Java class implementing a simplified ADT for undirected graphs. This class must be based
on the adjacency list structure, and it should implement a GraphADT interface especially 
provided for this exercise. The Java implementation should allow a new graph to be 
constructed from a collection of nodes and a collection of edges and should support
a number of basic operations, as detailed below.


-insertVertex(n) insert a new vertex with name n – return the new vertex
-removeVertex(v) remove the vertex v – return the vertex name
-insertEdge(v,w,n) Insert new edge with endpoints v and w and name n
-removeEdge(e) remove the edge e - return the edge name
-opposite(e,v) Return endpoint of edge e that is opposite endpoint v
-vertices() return an iterable collection of all the graph vertices
-edges() return an iterable collection of all the graph edges
-areAdjacent(v,w) return true if v, w are adjacent; false otherwise
-incidentEdges(v) return an iterable collection of the edges incident to v
-rename(v,n) rename existing vertex v as n; return old vertex name
-rename(e,n) rename existing edge e as n; return old edge name

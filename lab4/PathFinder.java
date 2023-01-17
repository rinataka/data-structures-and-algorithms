
import java.util.*;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.stream.Collectors;
import java.util.function.Function;

/**
 * This is a class that can find paths in a given graph.
 * 
 * There are several methods for finding paths, 
 * and they all return a PathFinder.Result object.
 */
public class PathFinder<Node> {

    private DirectedGraph<Node> graph;
    private long startTimeMillis;

    /**
     * Creates a new pathfinder for the given graph.
     * @param graph  the graph to search
     */
    public PathFinder(DirectedGraph<Node> graph) {
        this.graph = graph;
    }

    /**
     * The main search method, taking the search algorithm as input.
     * @param  algorithm  "random", "ucs" or "astar"
     * @param  start  the start node
     * @param  goal   the goal node
     */
    public Result search(String algorithm, Node start, Node goal) {
        startTimeMillis = System.currentTimeMillis();
        switch (algorithm) {
        case "random": return searchRandom(start, goal);
        case "ucs":    return searchUCS(start, goal);
        case "astar":  return searchAstar(start, goal);
        }
        throw new IllegalArgumentException("Unknown search algorithm: " + algorithm);
    }

    /**
     * Perform a random walk in the graph, hoping to reach the goal.
     * Warning: this method will give up of the random walk
     * reaches a dead end or after one million iterations.
     * So a negative result does not mean there is no path.
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchRandom(Node start, Node goal) {
        int iterations = 0;
        LinkedList<Node> path = new LinkedList<>();
        double cost = 0;
        Random random = new Random();

        Node current = start;
        while (iterations < 1e6) {
            iterations++;
            path.add(current);
            if (current.equals(goal))
                return new Result(true, start, current, cost, path, iterations);

            List<DirectedEdge<Node>> neighbours = graph.outgoingEdges(start);
            if (neighbours.size() == 0)
                break;

            DirectedEdge<Node> edge = neighbours.get(random.nextInt(neighbours.size()));
            cost += edge.weight();
            current = edge.to();
        }
        return new Result(false, start, goal, -1, null, iterations);
    }

    /**
     * Run the Uniform Cost Search algorithm for finding the shortest path.
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchUCS(Node start, Node goal) {
        int iterations = 0;
        Queue<PQEntry> pqueue = new PriorityQueue<>(Comparator.comparingDouble(entry -> entry.costToHere));
        /******************************
         * TODO: Task 1a+c            *
         * Change below this comment  *
         ******************************/

        pqueue.add(new PQEntry(start, 0,null ));
        List<Node> visitedNodes = new LinkedList<>();

        while (!pqueue.isEmpty()){
            iterations++;
            PQEntry entry = pqueue.remove();

            if (!visitedNodes.contains(entry.node)) {

                visitedNodes.add(entry.node);
                if (entry.node.equals(goal)) {
                    return new Result(true, start, goal, entry.costToHere, extractPath(entry), iterations);
                }

                for (DirectedEdge<Node> edge : graph.outgoingEdges(entry.node)) {
                    double costToNext = entry.costToHere + edge.weight();
                    pqueue.add(new PQEntry(edge.to(), costToNext, entry));
                }
            }

        }

        return new Result(false, start, goal, -1, null, iterations);
    }
    
    /**
     * Run the A* algorithm for finding the shortest path.
     * @param start  the start node
     * @param goal   the goal node
     */
    public Result searchAstar(Node start, Node goal) {
        int iterations = 0;
        /******************************
         * TODO: Task 3               *
         * Change below this comment  *
         ******************************/
        //initialise prio queue of nodes according to the estimated cost from start to end via the entered node
        Queue<PQEntry> pqueue = new PriorityQueue<>(Comparator.comparingDouble(entry -> entry.costEstimate));

        //initialise double for the cost to next node
       // double costToNext = 0;

        //adding start node to pqueue
        pqueue.add(new PQEntry(start, 0, graph.guessCost(start, goal), null));
        //initialise list of visited nodes so that they wont be visited again
        List<Node> visitedNodes = new LinkedList<>();

        //while the queue of nodes to explore isnt empty
        while (!pqueue.isEmpty()){
            //add 1 iteration
            iterations++;
            //remove node from top of queue
            PQEntry entry = pqueue.remove();

            //if node isn't already visited
            if (!visitedNodes.contains(entry.node)){

                //add node to list of visited nodes
                visitedNodes.add(entry.node);

                //if current node is the goal node
                if (entry.node.equals(goal)){
                    //return the result
                    return new Result(true, start, goal, entry.costToHere, extractPath(entry), iterations);
                }

                //for each outgoing edge in graph from the current node
                for (DirectedEdge<Node> edge : graph.outgoingEdges(entry.node)){
                    //calculate cost of the path from start to next node
                    double costToNext = entry.costToHere + edge.weight();
                    double estimate = costToNext + graph.guessCost(edge.to(), goal);


                    //add next node with the cost to get there from start node to priority queue to explore in next
                    // iteration of the while loop
                    pqueue.add(new PQEntry(edge.to(), costToNext, estimate, entry));
                }

            }

        }


        return new Result(false, start, goal, -1, null, iterations);
    }

    /**
     * Extract the final path from start to goal, from the final priority queue entry.
     * @param entry  the final priority queue entry
     * @return the path from start to goal as a list of nodes
     */
    private List<Node> extractPath(PQEntry entry) {
        LinkedList<Node> path = new LinkedList<>();
        /******************************
         * TODO: Task 1b              *
         * Change below this comment  *
         ******************************/

        path.add(entry.node);

        PQEntry current = entry;

        while (current.backPointer != null){
            path.add(current.backPointer.node);
            current = current.backPointer;
        }

        Collections.reverse(path);

        return path;
    }

    /**
     * Entries to put in the priority queues in {@code searchUCS} and {@code searchAstar}.
     */
    private class PQEntry {
        public final Node node;
        public final double costToHere;
        public final PQEntry backPointer;
        public final double costEstimate;
        /***********************************
         * TODO: Task 3                    *
         * Change below this comment,      *
         * for example, to add new fields. *
         **********************************/

        PQEntry(Node n, double c, double eC, PQEntry bp) {
            node = n;
            costToHere = c;
            backPointer = bp;
            costEstimate = eC;
        }

        PQEntry(Node n, double c, PQEntry bp) {
            node = n;
            costToHere = c;
            backPointer = bp;
            costEstimate = 0;
        }
    }

    /**
     * The internal class for search results.
     */
    public class Result {
        public final boolean success;
        public final Node start;
        public final Node goal;
        public final double cost;
        public final List<Node> path;
        public final int iterations;
        public final double elapsedTime;

        public Result(boolean success, Node start, Node goal, double cost, List<Node> path, int iterations) {
            this.success = success;
            this.start = start;
            this.goal = goal;
            this.cost = cost;
            this.path = path;
            this.iterations = iterations;
            this.elapsedTime = (System.currentTimeMillis() - startTimeMillis) / 1000.0;
        }

        @Override
        public String toString() {
            StringWriter buffer = new StringWriter();
            PrintWriter w = new PrintWriter(buffer);
            if (iterations <= 0)
                w.println("ERROR: You have to iterate over at least the starting node!");
            w.println("Loop iterations: " + iterations);
            w.println("Elapsed time: " + elapsedTime);
            if (success) {
                w.println("Total cost from " + start + " to " + goal + ": " + cost);
                int len = path.size();
                if (len == 0)
                    w.println("WARNING: you have not implemented extractPath!");
                else {
                    w.println("Total path length: " + (len - 1));
                    Function<List<Node>, String> joinPath = path ->
                        path.stream().map(Node::toString).collect(Collectors.joining(" -> "));
                    if (len < 10)
                        w.println("Path: " + joinPath.apply(path));
                    else
                        w.println("Path: " + joinPath.apply(path.subList(0, 5)) + " -> ... -> " + joinPath.apply(path.subList(len-5, len)));
                }
            } else
                w.println("No path found from " + start + " to " + goal);
            return buffer.toString();
        }
    }

}

README

WalkRouterApp is a command line application to calculate the shortest walking distance in meters between two given OSM nodes in a provided graph.

* Required arguments: valid file <path to graph> and two valid OSM nodes <from-osm-id> <to-osm-id>
* Printed to console: shortest walking distance (meters) between input OSM nodes

Input is parsed into Node and Edge. The shortest walking distance in meters is computed between two given OSM nodes in the graph (assuming all edges are walkable)

BUILD STATUS (Travis CI)

[![Build Status](https://travis-ci.com/KatieSanderson/WalkRouterApp.svg?branch=master)](https://travis-ci.com/KatieSanderson/WalkRouterApp)

RUN INSTRUCTIONS
1. If not installed on OS, install Java JDK and add to PATH
2. Navigate to project file in terminal
3. Run command: "./run.sh <path to graph> <from-osm-id> <to-osm-id>" where:
    * <path to graph> is the absolute or relative (with root of project folder) file path for valid file (see VALID FILE REQUIREMENTS section)
    * <from-osm-id> is the ID of the starting node to calculate shortest path (see VALID INPUT NODE REQUIREMENTS section)
    * <to-osm-id> is the ID of the ending node to calculate shortest path (see VALID INPUT NODE REQUIREMENTS section)

VALID FILE REQUIREMENTS
File will be format:
~~~~
<number of nodes>
<OSM id of node>
...
<OSM id of node>
<number of edges>
<from node OSM id> <to node OSM id> <length in meters>
...
<from node OSM id> <to node OSM id> <length in meters>
~~~~

VALID INPUT NODE REQUIREMENTS
1. Nodes with <OSM id of node> in file input
2. Nodes can be connected via edges (<from node OSM id> <to node OSM id> <length in meters>) in input

ASSUMPTIONS
1. Program arguments will contain valid file and two valid nodes
2. All edges are bi-directional and walkable
3. Input bounds:
    * <OSM id of node>: [0, 2^63 - 1]
        * Lower bound is 0; ids should be specified with positive values
        * Upper bound is 2^63 - 1; large capacity for values and improbability of requiring larger values to represent ids
    * <length in meters>: [0, 2^31 - 1]
        * Lower bound must be 0; negative distances are impossible in context but 0 would indicate two nodes in the same location
        * Upper bound is 2^31 - 1; large capacity for values and improbability of requiring larger values to represent distance
    * <number of nodes>: [2, 2^31 - 1]
        * Lower bound is 2; 2 nodes are required to run program without thrown exceptions
        * Upper bound is 2^31 - 1; large capacity for values and improbability of requiring larger values to represent number of nodes
    * <number of edges>: [1, 2^31 - 1]
        * Lower bound is 1; 1 edge is required to run program without thrown exceptions
        * Upper bound is 2^31 - 1; large capacity for values and improbability of requiring larger values to represent number of edges

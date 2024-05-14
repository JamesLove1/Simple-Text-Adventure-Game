package edu.uob.gameMap;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import edu.uob.entities.Artefacts;
import edu.uob.entities.Characters;
import edu.uob.entities.Furniture;
import edu.uob.entities.Locations;
//import jdk.incubator.vector.LongVector;

import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LoadDOTFile {
    
    private HashSet<String> entityNames;
    private String startLocation;
    private ArrayList<Graph> sections;
    private HashMap<String, Locations> location;

    public LoadDOTFile(File filePath){
        this.location = new HashMap<>();
        this.entityNames = new HashSet<>();
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(filePath);
            parser.parse(reader);
            Graph wholeDocument = parser.getGraphs().get(0);
            this.sections = wholeDocument.getSubgraphs();
        } catch (FileNotFoundException fnfe) {
            System.err.println("FileNotFoundException was thrown when attempting to read basic entities file");
        } catch (ParseException pe) {
            System.err.println("ParseException was thrown when attempting to read basic entities file");
        }
        extractLocations();
        extractPaths();
    }

    private void extractLocations(){
        ArrayList<Graph> locations = sections.get(0).getSubgraphs();
        for( int i = 0; i < sections.get(0).getSubgraphs().size(); i++){
            extractLocation(i);
        }

    }

    private void extractLocation(int subgraphNum){
        ArrayList<Node> subgraph = sections.get(0).getSubgraphs().get(subgraphNum).getNodes(false);
        String locationName = subgraph.get(0).getId().getId().toLowerCase();

        entityNames.add(locationName.toLowerCase());

        if(subgraphNum == 0 ){
            this.startLocation = locationName;
        }

        String description = subgraph.get(0).getAttributes().get("description"); // this is a way to remove the [] from the description
//        String description = subgraph.get(0).getAttributes().values().toString();
        Locations tempLocation = new Locations(locationName,description);
        this.location.put(locationName, tempLocation);
        for(int i = 0; i < sections.get(0).getSubgraphs().get(subgraphNum).getSubgraphs().size(); i++){
            String sectionName = sections.get(0).getSubgraphs().get(subgraphNum).getSubgraphs().get(i).getId().getId();
            if (sectionName.equals("artefacts") ){
                extractArtefacts(subgraphNum, locationName, i);
            }
            if (sectionName.equals("furniture")) {
                extractFurniture(subgraphNum, locationName, i);
            }
            if(sectionName.equals("characters")){
                extractCharacters(subgraphNum, locationName, i);
            }
        }
    }

    private void extractArtefacts(int subgraphNum, String locationName, int subSection){
        Graph tempLocation = sections.get(0).getSubgraphs().get(subgraphNum).getSubgraphs().get(subSection);
        ArrayList<Node> artifacts = tempLocation.getNodes(false);
        for(int i = 0 ; i < artifacts.size(); i++){
            String key = artifacts.get(i).getId().getId();
            key = key.toLowerCase();
            entityNames.add(key);
            String value = artifacts.get(i).getAttributes().get("description");
            Artefacts tempArtefats = new Artefacts(key,value);
            this.location.get(locationName).addArtefacts(key, tempArtefats);
        }

    }

    private void extractFurniture(int subgraphNum, String locationName, int subSection){
        Graph tempLocation = sections.get(0).getSubgraphs().get(subgraphNum).getSubgraphs().get(subSection);
        ArrayList<Node> furniture = tempLocation.getNodes(false);
        for(int i = 0 ; i < furniture.size(); i++){
            String key = furniture.get(i).getId().getId();
            key = key.toLowerCase();
            entityNames.add(key);
            String value = furniture.get(i).getAttributes().get("description");
            Furniture tempFurniture = new Furniture(key, value);
            this.location.get(locationName).addFurniture(key, tempFurniture);
        }
    }

    private void extractCharacters(int subgraphNum, String locationName, int subSection){
        Graph tempLocation = sections.get(0).getSubgraphs().get(subgraphNum).getSubgraphs().get(subSection);
        ArrayList<Node> character = tempLocation.getNodes(false);
        for(int i = 0 ; i < character.size(); i++){
            String key = character.get(i).getId().getId();
            entityNames.add(key);
            String value = character.get(i).getAttributes().get("description");
            Characters tempCharacter = new Characters(key, value);
            this.location.get(locationName).addCharacters(key, tempCharacter);
        }
    }

    private void extractPaths(){
        ArrayList<Edge> paths = sections.get(1).getEdges();
        Edge firstPath = paths.get(0);
        Node fromLocation = firstPath.getSource().getNode();
        String fromName = fromLocation.getId().getId();
        Node toLocation = firstPath.getTarget().getNode();
        String toName = toLocation.getId().getId();
        for(int i = 0; i < sections.get(1).getEdges().size(); i++){

            Locations from = this.location.get(paths.get(i).getSource().getNode().getId().getId().toLowerCase());
            Locations to = this.location.get(paths.get(i).getTarget().getNode().getId().getId().toLowerCase());
            this.location.get(from.getName()).addPathsTo(to.getName());

        }
    }

    public HashMap<String, Locations> getLocations(){
        return this.location;
    }

    public String getStartLocation(){ return this.startLocation;}
    
    public HashSet<String> getentityNames() { return this.entityNames;}

}

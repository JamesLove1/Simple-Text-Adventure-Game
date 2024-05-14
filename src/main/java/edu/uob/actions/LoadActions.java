package edu.uob.actions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

// notes
// hash set sibling trigger words

public class LoadActions {

    HashMap<String, HashSet<GameAction>> actions;

    public LoadActions(File actionsFile){
        this.actions = new HashMap<String, HashSet<GameAction>>();
        loadXML(actionsFile);
    }

    public void loadXML(File actionsFile){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(actionsFile.toString());
            Element root = document.getDocumentElement();
            NodeList actions = root.getChildNodes();

            for(int i = 0; i < actions.getLength(); i++){
                if( i%2 != 0 ){
                    Node action = root.getChildNodes().item(i);
                    addActionTrigger(action, i);
                }
            }
        } catch(ParserConfigurationException pce) {
            System.err.println("ParserConfigurationException was thrown when attempting to read basic actions file");
        } catch(SAXException saxe) {
            System.err.println("SAXException was thrown when attempting to read basic actions file");
        } catch(IOException ioe) {
            System.err.println("IOException was thrown when attempting to read basic actions file");
        }
    }

    private void addActionTrigger(Node action, int actionIndex){
        Integer maxLen = action.getChildNodes().item(1).getChildNodes().getLength();
        for(int i = 0; i < maxLen ; i++){
            if( i%2 != 0 ){
                addToHashMap(action);
            }
        }
    }

    private void addToHashMap (Node action){
        String triggerName;
        GameAction tempGameAction;
        HashSet<GameAction> tempGameSet;
        int maxLen = action.getChildNodes().item(1).getChildNodes().getLength();
        for(int i = 0; i<maxLen;i++){
            if( i%2 != 0 ){
                triggerName = action.getChildNodes().item(1).getChildNodes().item(i).getTextContent().toLowerCase();
                tempGameAction = new GameAction();

                tempGameSet = new HashSet<>();
                tempGameSet.add(tempGameAction);

                addSiblingTriggers(action,tempGameAction);
                addSubjects(action,tempGameAction);
                addConsumed(action,tempGameAction);
                addProduced(action,tempGameAction);
                addNaration(action,tempGameAction);

                if(this.actions.containsKey(triggerName)){
                    this.actions.get(triggerName).add(tempGameAction);
                } else {
                    this.actions.put(triggerName,tempGameSet);
                }

            }
        }

    }

    private void addSiblingTriggers(Node action, GameAction tempGameAction) {
        int maxLen = action.getChildNodes().item(1).getChildNodes().getLength();
        for (int i = 0; i < maxLen; i++) {
            if (i % 2 != 0) {
                String triggerName = action.getChildNodes().item(1).getChildNodes().item(i).getTextContent();
                tempGameAction.addSiblingTriggers(triggerName);
            }
        }

    }

    private void addSubjects(Node action, GameAction tempGameAction){
        int maxLen = action.getChildNodes().item(3).getChildNodes().getLength();
        for(int i = 0; i<maxLen;i++){
            if( i%2 != 0 ){
                String subject = action.getChildNodes().item(3).getChildNodes().item(i).getTextContent();
                subject = subject.toLowerCase();
                tempGameAction.addSubjects(subject);
            }
        }
    }

    private void addConsumed(Node action, GameAction tempGameAction){
        int maxLen = action.getChildNodes().item(5).getChildNodes().getLength();
        for(int i = 0; i<maxLen;i++){
            if( i%2 != 0 ){
                String consumed = action.getChildNodes().item(5).getChildNodes().item(i).getTextContent();
                consumed = consumed.toLowerCase();
                tempGameAction.addConsumed(consumed);
            }
        }
    }

    private void addProduced(Node action, GameAction tempGameAction){
        int maxLen = action.getChildNodes().item(7).getChildNodes().getLength();
        for(int i = 0; i<maxLen;i++){
            if( i%2 != 0 ){
                String produced = action.getChildNodes().item(7).getChildNodes().item(i).getTextContent();
                produced = produced.toLowerCase();
                tempGameAction.addProduced(produced);
            }
        }
    }

    private void addNaration(Node action, GameAction tempGameAction){
        tempGameAction.addNarration(action.getChildNodes().item(9).getTextContent());
    }

    public HashMap<String, HashSet<GameAction>> getActions(){
        return this.actions;
    }

}

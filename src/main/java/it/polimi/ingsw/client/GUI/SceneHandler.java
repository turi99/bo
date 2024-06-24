package it.polimi.ingsw.client.GUI;

/**
 * Interface of SceneHandlers
 */
public interface SceneHandler{
    void setClientGui(ClientGUI c);

    void setParameters(SceneHandler v);

    void init();
}
package iut.unice.dreamteam;

import java.util.ArrayList;

public class ApplicationStates {
    public static final int NONE = 93;
    public static final int CONNECT = 928;
    public static final int DELETE = 657;

    private static ApplicationStates applicationStates;
    private int currentState;
    private Object data;
    private ArrayList<StateChangeListener> stateChangeListeners;

    public static ApplicationStates getInstance(){
        if (applicationStates == null)
            applicationStates = new ApplicationStates();
        return applicationStates;
    }

    public ApplicationStates(){
        this.currentState = NONE;
        stateChangeListeners = new ArrayList<>();
    }

    public void setState(int state){
        data = null;

        this.currentState = state;
        for (StateChangeListener l : stateChangeListeners){
            l.stateChanged(NONE);
            l.stateChanged(state);
        }
    }

    public int getCurrentState() {
        return currentState;
    }

    public Boolean is(int state){
        return (state == currentState);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void addStateChangedListener(StateChangeListener stateChangeListener) {
        stateChangeListeners.add(stateChangeListener);
    }

    public ArrayList<StateChangeListener> getStateChangeListeners() {
        return stateChangeListeners;
    }

    public interface StateChangeListener{
        void stateChanged(int newState);
    }
}


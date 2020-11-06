package com.example.bookit;

import androidx.constraintlayout.solver.state.State;

import java.util.ArrayList;

public class RequestHandler {

    private BookState state;
    private ArrayList<String> requestors;
    private String acceptedRequestor;

    public RequestHandler() {
        this(new BookState(),new ArrayList<String>(), "");
    }

    public RequestHandler(BookState state) {
        this(state,new ArrayList<String>(), "");

    }
    /**
     * full constructor
     * @param state:  state of the book
     * @param pendingRequestors: list of pending requesters username
     * @param acceptedRequestor: the requester's username that is accepted
     */
    public RequestHandler(BookState state, ArrayList<String> pendingRequestors, String acceptedRequestor) {
        this.state = state;
        this.requestors = pendingRequestors;
        this.acceptedRequestor = acceptedRequestor;
    }

    /**
     * getter of BookState
     * @return BookState of the RequestHandler
     */
    public BookState getState() {
        return state;
    }

    /**
     * setter of BookState
     * @param state BookState intended to be set for the RequestHandler
     */
    public void setState(BookState state) {
        this.state = state;
    }


    /**
     * getter of Requestors
     * @return ArrayList contains all the requestors
     */
    public ArrayList<String> getRequestors() {
        return requestors;
    }

    /**
     * setter of Requestors
     * @param requestors Requestors intended to be set for the RequestHandler
     */
    public void setRequestors(ArrayList<String> requestors) {
        this.requestors = requestors;
    }

    /**
     * getter of acceptedRequestor
     * @return acceptedRequestor of the RequestHandler
     */
    public String getAcceptedRequestor() {
        return acceptedRequestor;
    }

    /**
     * setter of acceptedRequestor
     * @param acceptedRequestor acceptedRequestor intended to be set for the RequestHandler
     */
    public void setAcceptedRequestor(String acceptedRequestor) {
        this.acceptedRequestor = acceptedRequestor;
    }

}

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

    public BookState getState() {
        return state;
    }

    public void setState(BookState state) {
        this.state = state;
    }


    public ArrayList<String> getRequestors() {
        return requestors;
    }

    public void setRequestors(ArrayList<String> requestors) {
        this.requestors = requestors;
    }

    public String getAcceptedRequestor() {
        return acceptedRequestor;
    }

    public void setAcceptedRequestor(String acceptedRequestor) {
        this.acceptedRequestor = acceptedRequestor;
    }

}

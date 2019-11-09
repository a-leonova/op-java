package com.leonova;

import java.nio.CharBuffer;

public class DataSearcher implements Runnable {
    private final Object mutex;
    private final String searchText;

    private String previousIterationEnd = "";

    private boolean foundString = false;
    private boolean finishBufferProcessing = true;
    private boolean end = false;

    private char[] textBuffer = new char[10];
    private int read;

    public DataSearcher(Object mutex, String searchText) {
        this.mutex = mutex;
        this.searchText = searchText;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!finishBufferProcessing || !end){
            try {
                synchronized (mutex) {
                    while (finishBufferProcessing && !end) {
                        mutex.wait();
                    }
                    search();
                    finishBufferProcessing = true;
                    mutex.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void search(){
        var stringBuffer = previousIterationEnd + new String(textBuffer, 0, read);
        if(stringBuffer.contains(searchText)){
            foundString = true;
        } else {
            previousIterationEnd = stringBuffer.substring(Math.max(stringBuffer.length() - searchText.length(), 0));
        }
    }

    public boolean isFoundString() {
        return foundString;
    }

    public boolean isFinishBufferProcessing() {
        return finishBufferProcessing;
    }

    public void setTextBuffer(char[] textBuffer, int read) {
        this.textBuffer = textBuffer;
        this.read = read;
        this.finishBufferProcessing = false;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}

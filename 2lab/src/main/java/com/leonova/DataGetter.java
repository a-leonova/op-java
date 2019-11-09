package com.leonova;

import java.io.*;
import java.lang.reflect.Parameter;
import java.nio.CharBuffer;

public class DataGetter implements Runnable {
    private final static int BUFFER_SIZE = (int)Math.pow(2, 19);

    private final Object mutex;
    private final String searchText;

    private File file;
    private DataSearcher searcher;

    private char[] buffer1;
    private char[] buffer2;

    private boolean foundString = false;

    public DataGetter(File file, String searchText) {
        this.file = file;
        this.searchText = searchText;

        mutex = new Object();
        searcher = new DataSearcher(mutex, searchText);
        buffer1 = new char[BUFFER_SIZE];
        buffer2 = new char[BUFFER_SIZE];
    }

    @Override
    public void run() {
        try(var fileReader = new BufferedReader(new FileReader(file))){
            var searcherThread = new Thread(searcher);
            searcherThread.start();

            var read = fileReader.read(buffer1);

            int i;
            for(i = 0; read != -1; ++i){
                var fullBuffer = i % 2 == 0 ? buffer1 : buffer2;
                var bufferForRead = i % 2 == 0 ? buffer2 : buffer1;

                synchronized (mutex){
                    while (!searcher.isFinishBufferProcessing()){
                        mutex.wait();
                    }
                    if(searcher.isFoundString()){
                        break;
                    }
                    searcher.setTextBuffer(fullBuffer, read);
                    mutex.notify();
                }
                read = fileReader.read(bufferForRead);
            }
            synchronized (mutex){
                searcher.setEnd(true);
                mutex.notify();
            }
            searcherThread.join();
            foundString = searcher.isFoundString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isFoundString() {
        return foundString;
    }

    public File getFile() {
        return file;
    }
}

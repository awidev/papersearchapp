/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Hafiz & Aditya Gunawan
 */
public class Stopword {

    Vector<String> stopword = new Vector<String>();

    public Stopword() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader("src/Document/stopwords.txt");
        BufferedReader br = new BufferedReader(fr);
        String words = br.readLine();
        StringTokenizer Stoplist = new StringTokenizer(words);

        while (Stoplist.hasMoreTokens()) {
            String kt = Stoplist.nextToken();
            kt = kt.toLowerCase();
            stopword.add(kt);
        }

    }

    public String getStopword(int index) {
        return stopword.get(index);
    }

    public int size() {
        return stopword.size();
    }
}

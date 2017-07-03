/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocess;

import org.apache.lucene.analysis.id.IndonesianStemmer;


/**
 *
 * @author Hafiz & Aditya Gunawan
 */
public class Stemming {

    private IndonesianStemmer indoStemm;
    private int len = 0;

    public Stemming() {
        indoStemm = new IndonesianStemmer();
    }

    public String proses(String value) {
        char[] tes = value.toCharArray();
        len = indoStemm.stem(tes, tes.length, true);
        value = new String(tes, 0, len);
        return value;
    }
}

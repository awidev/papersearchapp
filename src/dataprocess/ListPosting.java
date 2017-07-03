/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocess;

import java.util.Vector;

/**
 *
 * @author Hafiz & Aditya Gunawan
 */
public class ListPosting {

    private Vector<String> Term;
    private Vector<Integer> Freq;
    private Vector<String> Stem;
    private Vector<String> Sumber;

    String temp = "";

    public ListPosting() {
        Term = new Vector<String>();
        Freq = new Vector<Integer>();
        Stem = new Vector<String>();
        Sumber = new Vector<String>();
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getSumber(int index) {
        return Sumber.get(index);
    }

    public void addSumber(String element) {
        Sumber.add(element);
    }

    public void setSumber(int index, String element) {
        Sumber.set(index, element);
    }

    public void addSumber(int index, String element) {
        Sumber.add(index, element);
    }

    public void addTerm(String Value) {
        Term.add(Value);
    }

    public void addStem(String Value) {
        Stem.add(Value);
    }

    public void addFreq(int index, int Element) {
        Freq.add(index, Element);
    }

    public void addFreq(int Element) {
        Freq.add(Element);
    }

    public void setFreq(int index, int Element) {
        Freq.set(index, Element);
    }

    public String getTerm(int Index) {
        return Term.get(Index);
    }

    public String getStem(int Index) {
        return Stem.get(Index);
    }

    public Integer getFreq(int Index) {
        return Freq.get(Index);
    }

    public Integer Size() {
        return Term.size();
    }

    public boolean isEmpty() {
        return Term.isEmpty();
    }

    public void remove(int index) {
        Term.remove(index);
        Freq.remove(index);
    }
}

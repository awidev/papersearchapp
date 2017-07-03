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
public class Extract {

    private String lokasiFolder;
    private Vector<String> DocTitle;
    private Vector<String> DocContent;

    public Extract() {
        DocTitle = new Vector<String>();
        DocContent = new Vector<String>();
    }

    public void setDatadoc(String Judul, String Isi) {
        DocTitle.add(Judul);
        DocContent.add(Isi);
    }

    public String getDocTitle(int index) {
        return DocTitle.get(index);
    }

    public String getDocContent(int index) {
        return DocContent.get(index);
    }

    public Integer getSize() {
        return DocTitle.size();
    }

    public void Remove() {
        for (int i = DocTitle.size(); i > 0; i--) {
            DocTitle.remove(i);
        }
        for (int i = DocContent.size(); i > 0; i--) {
            DocContent.remove(i);
        }
    }
}

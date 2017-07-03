/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataprocess;

/**
 *
 * @author Hafiz & Aditya Gunawan
 */
public class Detail {

    private int[][] listDetail;

    public Detail(int banyakterm, int banyakdoc) {
        listDetail = new int[banyakterm][banyakdoc];
    }

    public void setListDetail(int indexTerm, int indexDoc, int value) {
        this.listDetail[indexTerm][indexDoc] = value;
    }

    public int getListDetail(int indexTerm, int indexDoc) {
        return listDetail[indexTerm][indexDoc];
    }

    public int getSize() {
        return listDetail.length;
    }
}

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
public class RemoveTandaBaca {

    public RemoveTandaBaca() {
    }

    public String replace(String line) {
        line = line.replace(".", " ");
        line = line.replace("/", " ");
        line = line.replace("(", " ");
        line = line.replace(")", " ");
        line = line.replace(",", " ");
        line = line.replace("<", " ");
        line = line.replace(">", " ");
        line = line.replace("?", " ");
        line = line.replace("\'", " ");
        line = line.replace("\"", " ");
        line = line.replace("!", " ");
        line = line.replace("@", " ");
        line = line.replace("#", " ");
        line = line.replace("$", " ");
        line = line.replace("&", " ");
        line = line.replace(":", " ");
        line = line.replace(";", " ");
        line = line.replace("[", " ");
        line = line.replace("]", " ");
        line = line.replace("{", " ");
        line = line.replace("}", " ");
        line = line.replace("\\", " ");
        line = line.replace("|", " ");
        line = line.replace("+", " ");
        line = line.replace("=", " ");
        line = line.replace("-", " ");
        line = line.replace("*", " ");
        line = line.replace("^", " ");
        line = line.replace("%", " ");
        line = line.replace("`", " ");
        line = line.replace("~", " ");
        line = line.replace("𝑘", " ");
        line = line.replace("–", " ");
        line = line.replace("≥", " ");
        line = line.replace("”", " ");
        line = line.replace("·", " ");
        line = line.replace("�", " ");
        line = line.replace("’", " ");

        return line;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml.persistence;

/**
 *
 * @author marembo
 */
public class VXMLEntityManager<T> {

    public static class X {

        void println() {
            System.out.println("Printing in X-println: ");
        }
    }

    void printfoo(T t) {
        System.out.println("Printing foo: " + t.getClass());
    }

    public static class Bar<X> {

        VXMLEntityManager<X> xml = new VXMLEntityManager<X>() {

            void printfoo(X x) {
                System.out.println("Printing in derived:   " + x.getClass());
            }
        };
    }

    public static void main(String[] args) {
        Bar<String> b = new Bar<String>();
        b.xml.printfoo("hello");
    }
}

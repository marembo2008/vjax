/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml;

import com.anosym.vjax.exceptions.VXMLNamespaceException;

/**
 *
 * @author Marembo
 */
public final class VXMLVerifier {

    public static void verifyNamespace(VNamespace namespace) {
        //get the name part
        String name = namespace.getName();
        if (name.equalsIgnoreCase("xmlns")) {
            //get the prefix part
            String prf = namespace.getPrefix();
            if (prf != null && !prf.isEmpty() && prf.startsWith("xml")) {
                throw new VXMLNamespaceException("Invalid Namespace Declaration");
            }
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.io;

import com.anosym.vjax.VMarshaller;
import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.xml.VDocument;
import com.anosym.vjax.xml.VElement;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Administrator
 */
public class VXMLOutputStream<T> extends OutputStream {

    static final byte[] marker = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private final VMarshaller<T> vm = new VMarshaller<T>();
    private OutputStream output;

    public VXMLOutputStream(OutputStream output) {
        this.output = output;
    }

    public void writeObject(T o) throws VXMLBindingException, IOException {
        VElement elem = vm.marshall(o);
        VDocument doc = new VDocument("");
        doc.setRootElement(elem);
        String str = doc.toXmlString();
        byte[] data = str.getBytes();
//        byte[] bb = new byte[data.length + marker.length];
//        System.arraycopy(data, 0, bb, 0, data.length);
//        System.arraycopy(marker, 0, bb, data.length, marker.length);
        this.write(data);
    }

    @Override
    public void write(int b) throws IOException {
        this.output.write(b);
    }

    @Override
    public void close() throws IOException {
        output.close();
    }

    @Override
    public void flush() throws IOException {
        output.flush();
    }

    @Override
    public void write(byte[] b) throws IOException {
        output.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        output.write(b, off, len);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.io;

import com.anosym.vjax.VMarshaller;
import com.anosym.vjax.VXMLBindingException;
import com.anosym.vjax.util.ArrayUtil;
import com.anosym.vjax.xml.VDocument;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 *
 * @author Administrator
 */
public class VXMLInputStream<T> extends InputStream {

  private InputStream input;
  private Reader reader;
  private final VMarshaller<T> vm = new VMarshaller<T>();
  private byte[] marker = VXMLOutputStream.marker;

  public VXMLInputStream(Reader reader) {
    this.reader = reader;
  }

  public VXMLInputStream(InputStream input) {
    this.input = input;
  }

  public T readObject() throws VXMLBindingException, IOException {
    //read until end of marker
    byte[] buf = new byte[0];
    int i = 0, j = 0;
//        byte[] tmpMarker = new byte[marker.length];
    while (true) {
//            if (Arrays.equals(tmpMarker, marker)) {
//                break;
//            }
      i = read();
      if (i == -1) {
        //end of stream
        break;
      }
//            if (i == marker[0]) {
//                tmpMarker[j++] = (byte) i;
//            } else {
//                tmpMarker = new byte[marker.length];
//            }
      buf = ArrayUtil.copyOf(buf, buf.length + 1);
      buf[buf.length - 1] = (byte) i;
    }
    if (buf.length == 0) {
      throw new IOException("Unexpected end of input stream");
    }
//        if (marker.length == 0 || !Arrays.equals(marker, tmpMarker)) {
//            throw new IOException("Corrupted object input stream");
//        }
    //we need to remove the markers
//        byte[] data = new byte[buf.length - marker.length];
//        System.arraycopy(buf, 0, data, 0, data.length);
    ByteArrayInputStream inn = new ByteArrayInputStream(buf);
    VDocument doc = VDocument.parseDocument(inn);
    return vm.unmarshall(doc);
  }

  @Override
  public int read() throws IOException {
    if (input != null) {
      return input.read();
    } else if (reader != null) {
      return reader.read();
    }
    throw new IOException("Invalid InputStrem or Reader");
  }

  @Override
  public int available() throws IOException {
    if (input != null) {
      return input.available();
    }
    throw new IOException("Invalid InputStrem or Reader");
  }

  @Override
  public void close() throws IOException {
    if (input != null) {
      input.close();
    }
    if (reader != null) {
      reader.close();
    }
  }

  @Override
  public synchronized void mark(int readlimit) {
    if (reader != null) {
      try {
        reader.mark(readlimit);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    if (input != null) {
      input.mark(readlimit);
    }
  }

  @Override
  public boolean markSupported() {
    if (input != null) {
      return input.markSupported();
    } else if (reader != null) {
      return reader.markSupported();
    }
    return false;
  }

  @Override
  public int read(byte[] b) throws IOException {
    if (input != null) {
      return input.read(b);
    } else if (reader != null) {
      char[] cbuf = new char[b.length];
      int read = reader.read(cbuf);
      for (int i = 0; i < read; i++) {
        b[i] = (byte) cbuf[i];
      }
    }
    return 0;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    if (input != null) {
      return input.read(b, off, len);
    } else if (reader != null) {
      char[] cbuf = new char[b.length];
      int read = reader.read(cbuf, off, len);
      for (int i = 0; i < read; i++) {
        b[i] = (byte) cbuf[i];
      }
      return read;
    }
    return 0;
  }

  @Override
  public synchronized void reset() throws IOException {
    if (input != null) {
      input.reset();
    } else if (reader != null) {
      reader.reset();
    }
  }

  @Override
  public long skip(long n) throws IOException {
    if (input != null) {
      return input.skip(n);
    } else if (reader != null) {
      reader.skip(n);
    }
    return 0;
  }
}

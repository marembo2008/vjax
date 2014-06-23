/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anosym.vjax.xml;

import java.math.BigDecimal;

/**
 *
 * @author marembo
 */
public final class VContent extends VElement {

    private String content;

    public VContent(String content) {
        super("content");
        setContent(content);
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String toContent() {
        return getContent();
    }

    public static VContent empty() {
        return new VContent("");
    }

    public static VContent valueOf(String content) {
        return new VContent(content);
    }

    public static VContent valueOf(int content) {
        return new VContent(String.valueOf(content));
    }

    public static VContent valueOf(long content) {
        return new VContent(String.valueOf(content));
    }

    public static VContent valueOf(double content) {
        return new VContent(String.valueOf(content));
    }

    public static VContent valueOf(float content) {
        return new VContent(String.valueOf(content));
    }

    public static VContent valueOf(BigDecimal content) {
        return new VContent(String.valueOf(content));
    }

    public static VContent valueOf(Object content) {
        return new VContent(String.valueOf(content));
    }

    @Override
    public String toString() {
        return getContent();
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }
        VElement other = (VElement) obj;
        return getContent() == null ? other.getContent() == null : getContent().equals(other.getContent());
    }

    @Override
    public int hashCode() {
        int hash = 89 * ((getContent() != null) ? getContent().hashCode() : 0);
        return hash;
    }
}

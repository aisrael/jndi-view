/**
 * Copyright (c) 2011 by Alistair A. Israel
 *
 * This software is made available under the terms of the MIT License.
 *
 * Created Jan 10, 2011
 */
package jndi.view;

/**
 * @author Alistair A. Israel
 */
public class JndiEntry {

    private final String name;

    private final String className;

    private String targetClassName;

    private boolean context;

    private String link;

    /**
     * @param name
     *        the JNDI name
     * @param className
     *        the class name
     */
    public JndiEntry(final String name, final String className) {
        this.name = name;
        this.className = className;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @return the className
     */
    public final String getClassName() {
        return className;
    }

    /**
     * @return the targetClassName
     */
    public final String getTargetClassName() {
        return targetClassName;
    }

    /**
     * @param targetClassName
     *        the targetClassName to set
     */
    public final void setTargetClassName(final String targetClassName) {
        this.targetClassName = targetClassName;
    }

    /**
     * @return the targetClassName
     */
    public final String getActualClassName() {
        return targetClassName;
    }

    /**
     * @param targetClassName
     *        the targetClassName to set
     */
    public final void setActualClassName(final String actualClassName) {
        this.targetClassName = actualClassName;
    }

    /**
     * @return the context
     */
    public final boolean isContext() {
        return context;
    }

    /**
     * @param context
     *        the context to set
     */
    public final void setContext(final boolean context) {
        this.context = context;
    }

    /**
     * @return the link
     */
    public final String getLink() {
        return link;
    }

    /**
     * @param link
     *        the link to set
     */
    public final void setLink(final String link) {
        this.link = link;
    }

}

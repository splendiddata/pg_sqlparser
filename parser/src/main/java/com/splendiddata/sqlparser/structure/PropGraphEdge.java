/*
 * Copyright (c) Splendid Data Product Development B.V. 2020 - 2026
 *
 * This program is free software: You may redistribute and/or modify under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at Client's option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, Client should
 * obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.structure;

import com.splendiddata.sqlparser.enums.NodeTag;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Copied from /postgresql-19beta1/src/include/nodes/parsenodes.h
 *
 * @author Splendid Data Product Development B.V.
 * @since 19beta1
 */
@XmlRootElement(namespace = "parser")
public class PropGraphEdge extends Node {

    @XmlElement
    public RangeVar etable;

    @XmlElementWrapper(name = "ekey")
    @XmlElement(name = "ekeyElement")
    public List<Value> ekey;

    @XmlElementWrapper(name = "esrckey")
    @XmlElement(name = "esrckeyElement")
    public List<Value> esrckey;

    @XmlAttribute
    public String esrcvertex;

    @XmlElementWrapper(name = "esrcvertexcols")
    @XmlElement(name = "esrcvertexcol")
    public List<Value> esrcvertexcols;

    @XmlElementWrapper(name = "edestkey")
    @XmlElement(name = "edestkeyElement")
    public List<Value> edestkey;

    @XmlAttribute
    public String edestvertex;

    @XmlElementWrapper(name = "edestvertexcols")
    @XmlElement(name = "edestvertexcol")
    public List<Value> edestvertexcols;

    @XmlElementWrapper(name = "labels")
    @XmlElement(name = "label")
    public List<PropGraphLabelAndProperties> labels;

    /**
     * Constructor
     */
    public PropGraphEdge() {
        super(NodeTag.T_PropGraphEdge);
    }

    /**
     * Copy constructor
     *
     * @param original
     *            The PropGraphEdge to copy
     */
    public PropGraphEdge(PropGraphEdge original) {
        super(original);
        if (original.etable != null) {
            this.etable = original.etable.clone();
        }
        if (original.ekey != null) {
            this.ekey = original.ekey.clone();
        }
        if (original.esrckey != null) {
            this.esrckey = original.esrckey.clone();
        }
        this.esrcvertex = original.esrcvertex;
        if (original.esrcvertexcols != null) {
            this.esrcvertexcols = original.esrcvertexcols.clone();
        }
        if (original.edestkey != null) {
            this.edestkey = original.edestkey.clone();
        }
        this.edestvertex = original.edestvertex;
        if (original.edestvertexcols != null) {
            this.edestvertexcols = original.edestvertexcols.clone();
        }
        if (original.labels != null) {
            this.labels = original.labels.clone();
        }
    }

    @Override
    public PropGraphEdge clone() {
        PropGraphEdge clone = (PropGraphEdge) super.clone();
        if (etable != null) {
            clone.etable = etable.clone();
        }
        if (ekey != null) {
            clone.ekey = ekey.clone();
        }
        if (esrckey != null) {
            clone.esrckey = esrckey.clone();
        }
        if (esrcvertexcols != null) {
            clone.esrcvertexcols = esrcvertexcols.clone();
        }
        if (edestkey != null) {
            clone.edestkey = edestkey.clone();
        }
        if (edestvertexcols != null) {
            clone.edestvertexcols = edestvertexcols.clone();
        }
        if (labels != null) {
            clone.labels = labels.clone();
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (etable != null) {
            result.append(etable);
        }
        if (ekey != null && !ekey.isEmpty()) {
            result.append(" key ").append(ekey);
        }
        String separator = " source ";
        if (esrckey != null && !esrckey.isEmpty()) {
            result.append(separator).append("key ").append(esrckey);
            separator=" references ";
        }
        if (esrcvertex != null) {
            result.append(separator).append(esrcvertex);
        }
        if (esrcvertexcols != null && !esrcvertexcols.isEmpty()) {
            result.append(' ').append(esrcvertexcols);
        }
        separator = " destination ";
        if (edestkey != null && !edestkey.isEmpty()) {
            result.append(separator).append("key ").append(edestkey);
            separator=" references ";
        }
        if (edestvertex != null) {
            result.append(separator).append(edestvertex);
        }
        if (edestvertexcols != null && !edestvertexcols.isEmpty()) {
            result.append(' ').append(edestvertexcols);
        }
        if (labels != null && !labels.isEmpty()) {
            for (PropGraphLabelAndProperties label : labels) {
                String lbl = label.toString();
                if (!"".equals(lbl)) {
                    result.append(' ').append(lbl);
                }
            }
        }
        return result.toString();
    }

}

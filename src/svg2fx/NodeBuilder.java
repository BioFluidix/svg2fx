/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 by Alexander Heusel
 * 
 * This file is part of svgfx.
 *
 * svgfx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 * 
 * see: http://opensource.org/licenses/LGPL-3.0
 *
 * svgfx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * This version of svgfx includes copyright notice and attribution requirements.
 * According to the LGPL this information must be displayed even if you modify
 * the source code of svgfx. Neither the copyright statement nor the attribution
 * may be removed.
 *
 * Attribution Requirements:
 *
 * If you create derived work you must do two things regarding copyright notice
 * and author attribution.
 *
 * First, the copyright notice must remain. It must be reproduced in any program
 * that uses svgfx.
 *
 * Second, add an additional notice, stating that you modified svgfx. A suitable
 * notice might read "svgfx source code modified by YourName 2012".
 * 
 * Note, that these requirements are in full accordance with the LGPL v3
 * (see 7. Additional Terms, b).
 *
 */

package svg2fx;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import org.w3c.dom.svg.SVGCircleElement;
import org.w3c.dom.svg.SVGEllipseElement;
import org.w3c.dom.svg.SVGGElement;
import org.w3c.dom.svg.SVGLineElement;
import org.w3c.dom.svg.SVGPathElement;
import org.w3c.dom.svg.SVGPoint;
import org.w3c.dom.svg.SVGPointList;
import org.w3c.dom.svg.SVGPolylineElement;
import org.w3c.dom.svg.SVGRectElement;
import svg2fx.interfaces.ElementVisitor;

/**
 *
 * @author Alexander Heusel
 */
public class NodeBuilder implements ElementVisitor
{

    private TreeBuilder parent;
    
    public NodeBuilder(TreeBuilder parent)
    {
        this.parent = parent;
    }
    
    
    @Override
    public void visitSVGCircleElement(SVGCircleElement ce)
    {
        Circle circle = new Circle( ce.getCx().getBaseVal().getValue(),
                                    ce.getCy().getBaseVal().getValue(),
                                    ce.getR().getBaseVal().getValue());
        circle.setId(ce.getId());
        String att = ce.getAttribute("transform");
        if(att != null)
        {
            ArrayList<Transform> tr = Tools.decodeSVGTransforms(att);
            circle.getTransforms().addAll(tr);
        }
        System.out.format("[Circle] id: %s\n", ce.getId());
        parent.pushNode(circle);
    }

    @Override
    public void visitSVGEllipseElement(SVGEllipseElement ee)
    {
        Ellipse ellipse = new Ellipse(  ee.getCx().getBaseVal().getValue(),
                                        ee.getCy().getBaseVal().getValue(),
                                        ee.getRx().getBaseVal().getValue(),
                                        ee.getRy().getBaseVal().getValue());
        ellipse.setId(ee.getId());
        String att = ee.getAttribute("transform");
        if(att != null)
        {
            ArrayList<Transform> tr = Tools.decodeSVGTransforms(att);
            ellipse.getTransforms().addAll(tr);
        }
        System.out.format("[Ellipse] id: %s\n", ee.getId());
        parent.pushNode(ellipse);
    }

    @Override
    public void visitSVGLineElement(SVGLineElement le)
    {
        Line line = new Line(   le.getX1().getBaseVal().getValue(),
                                le.getY1().getBaseVal().getValue(),
                                le.getX2().getBaseVal().getValue(),
                                le.getY2().getBaseVal().getValue());
        line.setId(le.getId());
        String att = le.getAttribute("transform");
        if(att != null)
        {
            ArrayList<Transform> tr = Tools.decodeSVGTransforms(att);
            line.getTransforms().addAll(tr);
        }
        System.out.format("[Line] id: %s\n", le.getId());
        parent.pushNode(line);
    }

    @Override
    public void visitSVGRectElement(SVGRectElement re)
    {
        Rectangle rect = new Rectangle( re.getX().getBaseVal().getValue(),
                                        re.getY().getBaseVal().getValue(),
                                        re.getWidth().getBaseVal().getValue(),
                                        re.getHeight().getBaseVal().getValue());
        rect.setId(re.getId());
        String att = re.getAttribute("transform");
        if(att != null)
        {
            ArrayList<Transform> tr = Tools.decodeSVGTransforms(att);
            rect.getTransforms().addAll(tr);
        }

        System.out.format("[Rectangle] id: %s\n", re.getId());
        parent.pushNode(rect);
    }

    @Override
    public void visitSVGPolylineElement(SVGPolylineElement pe)
    {
        Polyline polyline = new Polyline();
        SVGPointList pl = pe.getPoints();
        SVGPoint cp;
        for(int i = 0; i < pl.getNumberOfItems(); i++)
        {
            cp = pl.getItem(i);
            polyline.getPoints().addAll((double)cp.getX(), (double)cp.getY());
        }
        polyline.setId(pe.getId());
        String att = pe.getAttribute("transform");
        if(att != null)
        {
            ArrayList<Transform> tr = Tools.decodeSVGTransforms(att);
            polyline.getTransforms().addAll(tr);
        }
        System.out.format("[Polyline] id: %s\n", pe.getId());
        parent.pushNode(polyline);
    }

    @Override
    public void visitSVGPathElement(SVGPathElement pe)
    {
        Path path = new Path();
        path.setId(pe.getId());
        String att = pe.getAttribute("transform");
        if(att != null)
        {
            ArrayList<Transform> tr = Tools.decodeSVGTransforms(att);
            path.getTransforms().addAll(tr);
        }
        
        System.out.format("[Path] id: %s\n", pe.getId());
        parent.pushNode(path);
    }

    @Override
    public void visitSVGGElement(SVGGElement svgGroup)
    {
        Group group = new Group();
        if(svgGroup != null)
        {
            group.setId(svgGroup.getId());
            String att = svgGroup.getAttribute("transform");
            if(att != null)
            {
                ArrayList<Transform> tr = Tools.decodeSVGTransforms(att);
                group.getTransforms().addAll(tr);
            }
            
            System.out.format("[Group] id: %s\n", svgGroup.getId());
        }
        else
        {
            System.out.println("[Group] id: root");
        }
        
        parent.pushNode(group);
    }

    @Override
    public void visitSVGGElementClose(SVGGElement svgGroup)
    {
        if(svgGroup != null)
        {
            System.out.format("[Close group] id: %s\n", svgGroup.getId());
        }
        else
        {
            System.out.println("[Close group] id: root");            
        }
        parent.closeGroup();
    }
    
}

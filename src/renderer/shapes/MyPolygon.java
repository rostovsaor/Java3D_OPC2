/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import renderer.point.MyPoint;
import renderer.point.PointConverter;

/**
 *
 * @author Salvador Rombe
 */
public class MyPolygon {
    
    private Color color;
    private MyPoint[] points;
    
    public MyPolygon(Color color, MyPoint... points) {
        this.color = color;
        this.points = new MyPoint[points.length];        
        for(int i = 0; i < points.length; i++) {
            MyPoint p = points[i];
            this.points[i] = new MyPoint(p.x, p.y, p.z);
        }
    }
    
    public MyPolygon(MyPoint... points) {
        this.color = Color.WHITE;        
        this.points = new MyPoint[points.length];        
        for(int i = 0; i < points.length; i++) {
            MyPoint p = points[i];
            this.points[i] = new MyPoint(p.x, p.y, p.z);
        }
    }
    
    
    public void render(Graphics g) {
        Polygon poly = new Polygon();
        for(int i = 0; i< this.points.length; i++) {
            Point p = PointConverter.convertPoint(this.points[i]);
            poly.addPoint(p.x, p.y);
        }
        
        g.setColor(this.color);
        g.fillPolygon(poly);
    }
    
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        for(MyPoint p : points) {
            PointConverter.rotateAxisX(p, CW, xDegrees);
//            PointConverter.rotateAxisY(p, CW, yDegrees);
//            PointConverter.rotateAxisZ(p, CW, zDegrees);
        }
    }
    
    public void setColor(Color color) {
        this.color = color;
    }

}

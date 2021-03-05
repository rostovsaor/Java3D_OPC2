/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package renderer.shapes;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Salvador Rombe
 */
public class Tetrahedron {
    
    private MyPolygon[] polygons;
    private Color color;
    
    public Tetrahedron(Color color, MyPolygon... polygons) {
        this.color = color;
        this.polygons = polygons;
        this.setPolygonColor();
    }
    
    public Tetrahedron(MyPolygon... polygons) {
        this.color = Color.WHITE;
        this.polygons = polygons;
    }
    
    public void render(Graphics g) {
        for(MyPolygon poly : this.polygons) {
            poly.render(g);
        }
    }
    
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees) {
        for(MyPolygon p : this.polygons) {
            p.rotate(CW, xDegrees, yDegrees, zDegrees);
        }
        this.sortPolygons();
    }
    
    private void sortPolygons() {
        
    }
    
    private void setPolygonColor() {
        for(MyPolygon poly : this.polygons) {
            poly.setColor(this.color);
        }
    }
}

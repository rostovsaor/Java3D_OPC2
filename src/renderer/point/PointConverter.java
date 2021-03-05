/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package renderer.point;

import java.awt.Point;
import renderer.Display;

/**
 *
 * @author Salvador Rombe
 */
public class PointConverter {
    
    private static double scale = 1;
    
    public static Point convertPoint(MyPoint point3D) {
        double x3d = point3D.y * scale;
        double y3d = point3D.z * scale;
        double depth = point3D.x * scale;
        
        double[] newVal = scale(x3d, y3d, depth);
//        int x2d = (int) (Display.WIDTH / 2 + point3D.y);
//        int y2d = (int) (Display.HEIGHT / 2 - point3D.z);
        int x2d = (int) (Display.WIDTH / 2 + newVal[0]);
        int y2d = (int) (Display.HEIGHT / 2 - newVal[1]);        
        Point point2D = new Point(x2d, y2d);
        return point2D;
    }
    private static double[] scale(double x3d, double y3d, double depth) {
        double dist = Math.sqrt(x3d*x3d + y3d*y3d);
        double theta = Math.atan2(y3d, x3d);
        double depth2 = 15 - depth;
        double localScale = Math.abs(1400/(depth2+1400));
        dist *= localScale;
        double[] newVal = new double[2];
        newVal[0] = dist * Math.cos(theta);
        newVal[1] = dist * Math.sin(theta);
        return newVal;
        
    }
    
    public static void rotateAxisX(MyPoint p, boolean CW, double degrees) {
        double radius = Math.sqrt(p.y*p.y + p.z*p.z);
        double theta = Math.atan2(p.y, p.z);
        theta += 2*Math.PI/360*degrees*(CW?1:-1);
        p.y = radius * Math.sin(theta);
        p.z = radius * Math.cos(theta);
    }
}

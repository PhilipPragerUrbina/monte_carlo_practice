package monte.carlo;

import java.util.Random;

//3d vector class
public class Vector3 implements Data{
    //values
    public double x;
    public double y;
    public double z;

    //constructors
    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //from scalar
    public Vector3(double scalar){
        this.x = scalar;
        this.y = scalar;
        this.z = scalar;
    }
    //zero vector
    public Vector3(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    //operators
    public Vector3 add(Vector3 b){
        return new Vector3(this.x+b.x, this.y+b.y, this.z+b.z);
    }
    public Vector3 subtract(Vector3 b){
        return new Vector3(this.x-b.x, this.y-b.y, this.z-b.z);
    }
    public Vector3 multiply(Vector3 b){
        return new Vector3(this.x*b.x, this.y*b.y, this.z*b.z);
    }
    public Vector3 divide(Vector3 b){
        return new Vector3(this.x/b.x, this.y/b.y, this.z/b.z);
    }

    //comparison
    public boolean equalsExact(Vector3 b){
        return this.x == b.x && this.y == b.y && this.z == b.z;
    }
    public boolean equals(Vector3 b, double delta){
        return deltaDouble(this.x , b.x, delta) && deltaDouble(this.y , b.y, delta) && deltaDouble(this.z , b.z, delta);
    }

    //utilities
    private static boolean deltaDouble(double a, double b, double d){
        return Math.abs(a-b) < d;
    }

    public double distance(Vector3 b){
        return Math.sqrt(Math.pow((this.x-b.x),2)+Math.pow((this.y-b.y),2)+Math.pow((this.z-b.z),2));
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vector3 normalized(){
        return this.divide(new Vector3(this.length()));
    }

    //random components between -0.5 and 0.5
    public static Vector3 randomVector(){
        return new Vector3(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
    }

    //get random normalized direction
    public static Vector3 randomDirection(){
        return randomVector().normalized();
    }

    public double dot(Vector3 b){
        return this.x * b.x + this.y * b.y + this.z * b.z;
    }

    //move in direction by distance
    Vector3 move(Vector3 dir, double dist){
        return this.add(dir.multiply(new Vector3(dist)));
    }

    @Override
    public double toNumericalValue() {
        return x;
    }

    @Override
    public String toStringValue() {
        return toString();
    }

    @Override
    public String toString() {
        return "(" + "," + x + "," + y + "," + z + ')';
    }
}


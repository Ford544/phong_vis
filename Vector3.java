public class Vector3{

    private double x;
    private double y;
    private double z;

    public Vector3(){
        x = y = z = 0;
    }

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 sum(Vector3 other){
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3 difference(Vector3 other){
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vector3 scale(double scalar){
        return new Vector3(scalar * this.x, scalar * this.y, scalar * this.z);
    }

    public double dot_product(Vector3 other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public double magnitude(){
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3 normalized(){
        double magni = magnitude();
        return new Vector3(this.x / magni, this.y / magni, this.z / magni);
    }
}
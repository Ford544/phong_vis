import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SphereRenderer {

    private static final int CHANNELS = 3;
    private static final Vector3 OBSERVATION_DIRECTION = new Vector3(0,0,1);

    private List<PointLightSource> pointLightSources;

    private Material sphereMaterial;
    private double radius;
    private double radius_squared;

    private int[] ambientMagnitudes;

    private Color backgroundColor;

    private double c0;
    private double c1;
    private double c2;

    private int res_x;
    private int res_y;


    private boolean self;
    private boolean diffuse;
    private boolean specular;
    private boolean ambience;


    private BufferedImage output;

    public SphereRenderer(){
        pointLightSources = new ArrayList<>();
        //pointLightSources.add(new PointLightSource(1, 1, -3, 255,255, 255));
        sphereMaterial = new Material(0.2, 1., 0.7,
                0.2, 1, 0.7,
                0.1, 0.1, 0.1,
                3,
                0,0,0);
        //Could we just implicitly assume that this is 1?
        radius = 1;
        radius_squared = radius*radius;
        ambientMagnitudes = new int[]{255,255,255};
        backgroundColor = new Color(100, 100, 240);
        c0 = 1;
        c1 = 1;
        c2 = 1;
        res_x = 600;
        res_y = 600;
        self = true;
        diffuse = true;
        specular = true;
        ambience = true;
        output = new BufferedImage(res_x, res_y, BufferedImage.TYPE_INT_RGB);
    }

    public void render(){

        for (int x = 0; x < res_x; x++){
            for (int y = 0; y < res_y; y++){
                Vector3 position = pixelToWorldSpacePoint(x,y);
                if (position == null) {
                    output.setRGB(x,y,backgroundColor.getRGB());
                } else {
                    double[] colors = new double[CHANNELS];
                    for (int c = 0; c < CHANNELS; c++){
                        colors[c] = 0;
                        if (self) colors[c] += selfLuminanceComponent(c);
                        if (ambience) colors[c] += ambienceComponent(c);
                    }

                    //Assumes that the center of the sphere is the center of the coordinate system, and
                    //therefore the normal of any point on the sphere has same direction as its position
                    Vector3 normal = position.normalized();

                    double[] specularComponents = new double[CHANNELS];
                    double[] diffuseComponents = new double[CHANNELS];
                    for (PointLightSource lightSource : pointLightSources){
                        double distance = position.difference(lightSource.position).magnitude();

                        Vector3 directionToSource = lightSource.position.difference(position).normalized();
                        Vector3 mirroredObservationDirection = OBSERVATION_DIRECTION.difference(normal.scale(2*OBSERVATION_DIRECTION.dot_product(normal)));

                        double cosine = normal.dot_product(directionToSource);
                        if (cosine < 0) continue;

                        double diffuseMultiplier = 0;
                        if (diffuse) diffuseMultiplier = diffuseMultiplier(distance, normal, directionToSource);
                        //System.out.println(diffuseMultiplier);
                        double specularMultiplier = 0;
                        if (specular) specularMultiplier = specularMultiplier(distance, directionToSource, mirroredObservationDirection);

                        for (int c = 0; c < CHANNELS; c++){
                            if (diffuse) diffuseComponents[c] += lightSource.magnitudes[c] * diffuseMultiplier;
                            if (specular) specularComponents[c] += lightSource.magnitudes[c] * specularMultiplier;
                        }
                    }



                    for (int c = 0; c < CHANNELS; c++){
                        colors[c] += diffuseComponents[c] * sphereMaterial.diffuseCoeffs[c];
                        colors[c] += specularComponents[c] * sphereMaterial.specularCoeffs[c];
                        if (colors[c] >= 256) colors[c] = 255;
                    }
                    output.setRGB(x, y, int2RGB((int) colors[0], (int) colors[1], (int) colors[2]));
                }
            }
        }
    }

    public void addLightSource(){
        pointLightSources.add(new PointLightSource(0, 0, 0, 255,255, 255));
    }

    public void removeLightSource(){
        pointLightSources.remove(pointLightSources.size()-1);
    }

    private double selfLuminanceComponent(int channel) {
        return sphereMaterial.selfLuminance[channel];
    }

    private double ambienceComponent(int channel){
        return ambientMagnitudes[channel] * sphereMaterial.ambientCoeffs[channel];
    }

    private double specularMultiplier(double distance, Vector3 direction, Vector3 mirroredObservationDirection){
        double cosine = direction.dot_product(mirroredObservationDirection);
        if (cosine < 0) return 0;
        return attenuationFactor(distance)*Math.pow(cosine, sphereMaterial.glossiness);
    }

    private double diffuseMultiplier(double distance, Vector3 normal, Vector3 direction){
        return attenuationFactor(distance)*normal.dot_product(direction);
    }

    private Vector3 pixelToWorldSpacePoint(int x, int y){
        double world_x = radius * (2. * x / (res_x-1) - 1);
        double world_y = radius * (2. * y / (res_y-1) - 1);
        double r = world_x*world_x + world_y*world_y;
        if (r > radius_squared) return null;
        return new Vector3(world_x, world_y, -Math.sqrt(radius_squared - r));
    }

    private double attenuationFactor(double r){
        return Math.min(1, 1 / (c2 * r * r + c1 * r + c0));
    }

    public boolean save(String filepath){
        try
        {
            ImageIO.write( output, "bmp", new File( filepath ));
            System.out.println( "Image created successfully");
            return true;
        }
        catch (IOException e)
        {
            System.out.println( "The image cannot be stored" );
            return false;
        }
    }

    private static int int2RGB( int red, int green, int blue)
    {
        // Make sure that color intensities are in 0..255 range
        red = red & 0x000000FF;
        green = green & 0x000000FF;
        blue = blue & 0x000000FF;
        // Assemble packed RGB using bit shift operations
        return (red << 16) + (green << 8) + blue;
    }

    public BufferedImage getOutput() {
        return output;
    }

    public void setAmbientMagnitudes(int[] magnitudes){
        ambientMagnitudes = magnitudes;
    }

    public void setSelfIntensity(int[] magnitudes){
        sphereMaterial.selfLuminance = magnitudes;
    }

    public void setGlossiness(double value){
        sphereMaterial.glossiness = value;
    }

    public void setAmbientCoeffs(double[] coeffs){
        sphereMaterial.ambientCoeffs = coeffs;
    }

    public void setSpecularCoeffs(double[] coeffs){
        sphereMaterial.specularCoeffs = coeffs;
    }

    public void setDiffuseCoeffs(double[] coeffs){
        sphereMaterial.diffuseCoeffs = coeffs;
    }

    public void setLightPosition(int index, Vector3 position){
        pointLightSources.get(index).position = position;
    }

    public void setLightIntensity(int index, int[] magnitudes) {
        pointLightSources.get(index).magnitudes = magnitudes;
    }

    public void setC0(double c0) {
        this.c0 = c0;
    }

    public void setC1(double c1) {
        this.c1 = c1;
    }

    public void setC2(double c2) {
        this.c2 = c2;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public void setDiffuse(boolean diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(boolean specular) {
        this.specular = specular;
    }

    public void setAmbience(boolean ambience) {
        this.ambience = ambience;
    }

    static class Material{

        double[] diffuseCoeffs;
        double[] specularCoeffs;
        double[] ambientCoeffs;
        double glossiness;
        int[] selfLuminance;

        public Material(double diff_r, double diff_g, double diff_b,
                        double spec_r, double spec_g, double spec_b,
                        double amb_r, double amb_g, double amb_b,
                        double g,
                        int selfLum_r, int selfLum_g, int selfLum_b){
            diffuseCoeffs = new double[] {diff_r, diff_g, diff_b};
            specularCoeffs = new double[] {spec_r, spec_g, spec_b};
            ambientCoeffs = new double[] {amb_r, amb_g, amb_b};
            glossiness = g;
            selfLuminance = new int[] {selfLum_r, selfLum_g, selfLum_b};
        }

    }


    static class PointLightSource{
        Vector3 position;
        int[] magnitudes;

        public PointLightSource(double x, double y, double z, int r, int g, int b){
            this.position = new Vector3(x, y, z);
            this.magnitudes = new int[]{r,g,b};
        }

    }

}

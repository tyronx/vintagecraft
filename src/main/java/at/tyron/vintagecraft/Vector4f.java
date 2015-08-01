package at.tyron.vintagecraft;

public class Vector4f extends Vector3f  {

    public float w;
    
    /**
        Creates a new Vector4f at (0,0,0,0).
    */
    public Vector4f() {
        super(0,0,0);
        w = 0;
    }


    /**
        Creates a new Vector4f with the same values as the
        specified Vector4f.
    */
    public Vector4f(Vector4f v) {
        this(v.x, v.y, v.z, v.w);
    }


    /**
        Creates a new Vector4f with the specified (x, y, z, w) values.
    */
    public Vector4f(float x, float y, float z, float w) {
        super.setTo(x, y, z);
        this.w = w;
    }


    
    /**
        Sets the vector to the same values as the specified
        Vector4f.
    */
    public void setTo(Vector4f v) {
        super.setTo(v.x, v.y, v.z);
        this.w = v.w;
    }
    
    public void setTo(float x, float y, float z, float w) {
        super.setTo(x, y, z);
        this.w = w;
    }
}
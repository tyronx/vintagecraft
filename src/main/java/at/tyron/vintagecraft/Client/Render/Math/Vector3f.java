package at.tyron.vintagecraft.Client.Render.Math;

public class Vector3f
{

        
          /** Vector X. **/
        public float x;
        
          /** Vector Y. **/
        public float y;
        
          /** Vector Z. **/
        public float z;


        /** Vector3f constructor.
         *  @param x Vector X.
         *  @param y Vector Y.
         *  @param z Vector Z.
         */

        public Vector3f(float x, float y, float z)
        {
                setTo(x, y, z);
        }
        
    /**
        Creates a new Vector3D with the same values as the
        specified Vector3D.
    */
    public Vector3f(Vector3f v) 
        {
                setTo(v);
        }

    /** Vector3f constructor.
         */
    public Vector3f()
        {
                this.x = 0.0f;
                this.y = 0.0f;
                this.z = 0.0f;
        }

    public void setTo(Vector3f v) 
        {
                this.x = v.x;
                this.y = v.y;
                this.z = v.z;
        
        }
    
    public void setTo(float x, float y, float z) 
        {
                this.x = x;
                this.y = y;
                this.z = z;

        }
    
    public void setZero()
    {
        this.x = 0.0f;
                this.y = 0.0f;
                this.z = 0.0f;
    
    }
    
    public void negate()
    {
        this.x = - this.x;
                this.y = - this.y;
                this.z = - this.z;
    }
}
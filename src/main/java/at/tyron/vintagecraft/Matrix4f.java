package at.tyron.vintagecraft;

public class Matrix4f {

	public float[] matrix = new float[16];
	private final static float PI_VALUE = 3.141592654f;
	
	
	        public Matrix4f() {
	                clear();
	        }
	
	        public Matrix4f(Matrix4f m) {
	        this(m.matrix[0], m.matrix[1], m.matrix[2], m.matrix[3],
	             m.matrix[4], m.matrix[5], m.matrix[6], m.matrix[7],
	             m.matrix[8], m.matrix[9], m.matrix[10], m.matrix[11],
	             m.matrix[12], m.matrix[13], m.matrix[14], m.matrix[15]);   
	        }
	
	        public Matrix4f(float r11, float r12, float r13, float r14,
	                        float r21, float r22, float r23, float r24,
	                        float r31, float r32, float r33, float r34,
	                        float r41, float r42, float r43, float r44)     {
	
	                
	        // To set the matrix identity you set all the values in the matrix like so...
	           matrix[0]  = r11; matrix[1]  = r12; matrix[2]  = r13; matrix[3]  = r14;
	           matrix[4]  = r21; matrix[5]  = r22; matrix[6]  = r23; matrix[7]  = r24;
	           matrix[8]  = r31; matrix[9]  = r32; matrix[10] = r33; matrix[11] = r34;
	           matrix[12] = r41; matrix[13] = r42; matrix[14] = r43; matrix[15] = r44;
	        }
	
	
	
	 public Matrix4f subtractMatrix(Matrix4f m)
	{
	   // Return the value of this matrix - m.
	   return new Matrix4f(matrix[0] - m.matrix[0], matrix[1] - m.matrix[1], matrix[2] - m.matrix[2],
	                     matrix[3] - m.matrix[3], matrix[4] - m.matrix[4], matrix[5] - m.matrix[5],
	                     matrix[6] - m.matrix[6], matrix[7] - m.matrix[7], matrix[8] - m.matrix[8],
	                     matrix[9] - m.matrix[9], matrix[10] - m.matrix[10], matrix[11] - m.matrix[11],
	                     matrix[12] - m.matrix[12], matrix[13] - m.matrix[13],
	                     matrix[14] - m.matrix[14], matrix[15] - m.matrix[15]);
	}
	
	public Matrix4f addMatrix(Matrix4f m)
	{
	   // Return the value of this matrix + m.
	   return new Matrix4f(matrix[0] + m.matrix[0], matrix[1] + m.matrix[1], matrix[2] + m.matrix[2],
	                     matrix[3] + m.matrix[3], matrix[4] + m.matrix[4], matrix[5] + m.matrix[5],
	                     matrix[6] + m.matrix[6], matrix[7] + m.matrix[7], matrix[8] + m.matrix[8],
	                     matrix[9] + m.matrix[9], matrix[10] + m.matrix[10], matrix[11] + m.matrix[11],
	                     matrix[12] + m.matrix[12], matrix[13] + m.matrix[13],
	                     matrix[14] + m.matrix[14], matrix[15] + m.matrix[15]);
	}
	
	
	public Matrix4f multiplyMatrix(Matrix4f m)
	{
	   // Return the value of this Matrix * m.
	   return new Matrix4f(matrix[0] * m.matrix[0] + matrix[4] * m.matrix[1] + matrix[8] * m.matrix[2] + matrix[12] * m.matrix[3],
	                                       matrix[1] * m.matrix[0] + matrix[5] * m.matrix[1] + matrix[9] * m.matrix[2] + matrix[13] * m.matrix[3],
	                       matrix[2] * m.matrix[0] + matrix[6] * m.matrix[1] + matrix[10] * m.matrix[2] + matrix[14] * m.matrix[3],
	                       matrix[3] * m.matrix[0] + matrix[7] * m.matrix[1] + matrix[11] * m.matrix[2] + matrix[15] * m.matrix[3],
	                       matrix[0] * m.matrix[4] + matrix[4] * m.matrix[5] + matrix[8] * m.matrix[6] + matrix[12] * m.matrix[7],
	                       matrix[1] * m.matrix[4] + matrix[5] * m.matrix[5] + matrix[9] * m.matrix[6] + matrix[13] * m.matrix[7],
	                       matrix[2] * m.matrix[4] + matrix[6] * m.matrix[5] + matrix[10] * m.matrix[6] + matrix[14] * m.matrix[7],
	                       matrix[3] * m.matrix[4] + matrix[7] * m.matrix[5] + matrix[11] * m.matrix[6] + matrix[15] * m.matrix[7],
	                       matrix[0] * m.matrix[8] + matrix[4] * m.matrix[9] + matrix[8] * m.matrix[10] + matrix[12] * m.matrix[11],
	                       matrix[1] * m.matrix[8] + matrix[5] * m.matrix[9] + matrix[9] * m.matrix[10] + matrix[13] * m.matrix[11],
	                       matrix[2] * m.matrix[8] + matrix[6] * m.matrix[9] + matrix[10] * m.matrix[10] + matrix[14] * m.matrix[11],
	                       matrix[3] * m.matrix[8] + matrix[7] * m.matrix[9] + matrix[11] * m.matrix[10] + matrix[15] * m.matrix[11], 
	                       matrix[0] * m.matrix[12] + matrix[4] * m.matrix[13] + matrix[8] * m.matrix[14] + matrix[12] * m.matrix[15],
	                       matrix[1] * m.matrix[12] + matrix[5] * m.matrix[13] + matrix[9] * m.matrix[14] + matrix[13] * m.matrix[15],
	                       matrix[2] * m.matrix[12] + matrix[6] * m.matrix[13] + matrix[10] * m.matrix[14] + matrix[14] * m.matrix[15],
	                       matrix[3] * m.matrix[12] + matrix[7] * m.matrix[13] + matrix[11] * m.matrix[14] + matrix[15] * m.matrix[15]);
	        
	                }
	
	public Matrix4f divideMatrix(Matrix4f m)
	{
	   // Return the value of this Matrix / m.
	   return new Matrix4f(matrix[0] / m.matrix[0] + matrix[4] / m.matrix[1] + matrix[8] / m.matrix[2] + matrix[12] / m.matrix[3],
	                                           matrix[1] / m.matrix[0] + matrix[5] / m.matrix[1] + matrix[9] / m.matrix[2] + matrix[13] / m.matrix[3],
	                       matrix[2] / m.matrix[0] + matrix[6] / m.matrix[1] + matrix[10] / m.matrix[2] + matrix[14] / m.matrix[3],
	                       matrix[3] / m.matrix[0] + matrix[7] / m.matrix[1] + matrix[11] / m.matrix[2] + matrix[15] / m.matrix[3],
	                       matrix[0] / m.matrix[4] + matrix[4] / m.matrix[5] + matrix[8] / m.matrix[6] + matrix[12] / m.matrix[7],
	                       matrix[1] / m.matrix[4] + matrix[5] / m.matrix[5] + matrix[9] / m.matrix[6] + matrix[13] / m.matrix[7],
	                       matrix[2] / m.matrix[4] + matrix[6] / m.matrix[5] + matrix[10] / m.matrix[6] + matrix[14] / m.matrix[7],
	                       matrix[3] / m.matrix[4] + matrix[7] / m.matrix[5] + matrix[11] / m.matrix[6] + matrix[15] / m.matrix[7],
	                       matrix[0] / m.matrix[8] + matrix[4] / m.matrix[9] + matrix[8] / m.matrix[10] + matrix[12] / m.matrix[11],
	                       matrix[1] / m.matrix[8] + matrix[5] / m.matrix[9] + matrix[9] / m.matrix[10] + matrix[13] / m.matrix[11],
	                       matrix[2] / m.matrix[8] + matrix[6] / m.matrix[9] + matrix[10] / m.matrix[10] + matrix[14] / m.matrix[11],
	                       matrix[3] / m.matrix[8] + matrix[7] / m.matrix[9] + matrix[11] / m.matrix[10] + matrix[15] / m.matrix[11], 
	                       matrix[0] / m.matrix[12] + matrix[4] / m.matrix[13] + matrix[8] / m.matrix[14] + matrix[12] / m.matrix[15],
	                       matrix[1] / m.matrix[12] + matrix[5] / m.matrix[13] + matrix[9] / m.matrix[14] + matrix[13] / m.matrix[15],
	                       matrix[2] / m.matrix[12] + matrix[6] / m.matrix[13] + matrix[10] / m.matrix[14] + matrix[14] / m.matrix[15],
	                       matrix[3] / m.matrix[12] + matrix[7] / m.matrix[13] + matrix[11] / m.matrix[14] + matrix[15] / m.matrix[15]);
	        
	                }
	
	public Matrix4f subtract(float f)
	{
	   return new Matrix4f(matrix[0]  - f, matrix[1]  - f, matrix[2]  - f, matrix[3]  - f,
	                     matrix[4]  - f, matrix[5]  - f, matrix[6]  - f, matrix[7]  - f,
	                     matrix[8]  - f, matrix[9]  - f, matrix[10] - f, matrix[11] - f,
	                     matrix[12] - f, matrix[13] - f, matrix[14] - f, matrix[15] - f);
	}
	
	public Vector3f getEulerAngles()
	{
	        //%%%%%%%%%%%%%
	        // NOT WORKING
	        //%%%%%%%%%%%%%
	
	        double angle_x, angle_y, angle_z;               // temp angles
	        double cy, tx, ty, sy;
	/*
	        angle_y = -asin(m[2]);          // calculate y axis angle
	        cy              = cos(angle_y);
	*/
	
	        sy      = -matrix[2];
	        cy      = Math.sqrt(1 - sy*sy);
	        angle_y = Math.atan2(sy, cy);
	
	
	        if (Quaternion.EPSILON < Math.abs(cy))          // no gimbal lock?
	        {
	                tx      = matrix[10] / cy;              // get x axis angle
	                ty      = matrix[6] / cy;
	
	                angle_x = Math.atan2(ty, tx);
	
	                tx      = matrix[0] / cy;               // get z axis angle
	                ty      = matrix[1] / cy;
	
	                angle_z = Math.atan2(ty, tx);
	        }
	        else            // gimbal lock
	        {
	                angle_x = 0.0f;                 // set x axis angle to 0
	
	                tx      = matrix[9];                            // get z axis angle
	                ty      = matrix[5];
	
	                angle_z = Math.atan2(ty, tx);
	        }
	
	        
	
	        return new Vector3f((float)Math.toDegrees(angle_x), (float)Math.toDegrees(angle_y), (float)Math.toDegrees(angle_z));
	}
	
	public Matrix4f add(float f)
	{
	   return new Matrix4f(matrix[0]  + f, matrix[1]  + f, matrix[2]  + f, matrix[3]  + f,
	                     matrix[4]  + f, matrix[5]  + f, matrix[6]  + f, matrix[7]  + f,
	                     matrix[8]  + f, matrix[9]  + f, matrix[10] + f, matrix[11] + f,
	                     matrix[12] + f, matrix[13] + f, matrix[14] + f, matrix[15] + f);
	}
	
	/*
	public void add(float f)
	{
	                     (matrix[0]  + f, matrix[1]  + f, matrix[2]  + f, matrix[3]  + f,
	                     matrix[4]  + f, matrix[5]  + f, matrix[6]  + f, matrix[7]  + f,
	                     matrix[8]  + f, matrix[9]  + f, matrix[10] + f, matrix[11] + f,
	                     matrix[12] + f, matrix[13] + f, matrix[14] + f, matrix[15] + f);
	}
	*/
	
	public Matrix4f multiply(float f)
	{
	   return new Matrix4f(matrix[0]  * f, matrix[1]  * f, matrix[2]  * f, matrix[3]  * f,
	                     matrix[4]  * f, matrix[5]  * f, matrix[6]  * f, matrix[7]  * f,
	                     matrix[8]  * f, matrix[9]  * f, matrix[10] * f, matrix[11] * f,
	                     matrix[12] * f, matrix[13] * f, matrix[14] * f, matrix[15] * f);
	}
	
	/*
	public void multiply(float f)
	{
	                     (matrix[0]  * f, matrix[1]  * f, matrix[2]  * f, matrix[3]  * f,
	                     matrix[4]  * f, matrix[5]  * f, matrix[6]  * f, matrix[7]  * f,
	                     matrix[8]  * f, matrix[9]  * f, matrix[10] * f, matrix[11] * f,
	                     matrix[12] * f, matrix[13] * f, matrix[14] * f, matrix[15] * f);
	}
	*/
	
	public Matrix4f divide(float f)
	{
	   // Return the value of this vector / f.  We do this by multiplying the recip.
	   if(f == 0) 
	        f = 1;
	   
	        f = 1/f;
	
	   return new Matrix4f(matrix[0]  * f, matrix[1]  * f, matrix[2]  * f, matrix[3]  * f,
	                     matrix[4]  * f, matrix[5]  * f, matrix[6]  * f, matrix[7]  * f,
	                     matrix[8]  * f, matrix[9]  * f, matrix[10] * f, matrix[11] * f,
	                     matrix[12] * f, matrix[13] * f, matrix[14] * f, matrix[15] * f);
	}
	
	/*
	public void divide(float f)
	{
	   // Return the value of this vector / f.  We do this by multiplying the recip.
	   if(f == 0) 
	        f = 1;
	   
	        f = 1/f;
	
	                    (matrix[0]  * f, matrix[1]  * f, matrix[2]  * f, matrix[3]  * f,
	                     matrix[4]  * f, matrix[5]  * f, matrix[6]  * f, matrix[7]  * f,
	                     matrix[8]  * f, matrix[9]  * f, matrix[10] * f, matrix[11] * f,
	                     matrix[12] * f, matrix[13] * f, matrix[14] * f, matrix[15] * f);
	}
	*/
	
	
	public boolean equals(Matrix4f m)
	{
	   // Return true if all equal each other, false if one or more don't.
	   for(int i = 0; i < 16; i++)
	      {
	         if(matrix[i] != m.matrix[i])
	            return false;
	      }
	
	   return true;
	}
	
	
	public void clear()
	{
	   // To set the matrix identity you set all the values in the matrix like so...
	        matrix[0]  = 1.0f; matrix[1]  = 0.0f; matrix[2]  = 0.0f; matrix[3]  = 0.0f;
	        matrix[4]  = 0.0f; matrix[5]  = 1.0f; matrix[6]  = 0.0f; matrix[7]  = 0.0f;
	        matrix[8]  = 0.0f; matrix[9]  = 0.0f; matrix[10] = 1.0f; matrix[11] = 0.0f;
	        matrix[12] = 0.0f; matrix[13] = 0.0f; matrix[14] = 0.0f; matrix[15] = 1.0f;
	
	   // If you remember matrices from math class you will notice that there is a diagonal
	   // line of 1's at matrix[0], [5], [10], and [15].  This is how you reset a matrix.
	}
	
	
	public void zero()
	{
	   // To set the matrix to zero you set all the values in the matrix like so...
	        matrix[0]  = 0.0f; matrix[1]  = 0.0f; matrix[2]  = 0.0f; matrix[3]  = 0.0f;
	        matrix[4]  = 0.0f; matrix[5]  = 0.0f; matrix[6]  = 0.0f; matrix[7]  = 0.0f;
	        matrix[8]  = 0.0f; matrix[9]  = 0.0f; matrix[10] = 0.0f; matrix[11] = 0.0f;
	        matrix[12] = 0.0f; matrix[13] = 0.0f; matrix[14] = 0.0f; matrix[15] = 0.0f;
	}
	
	public void translate(Vector4f translate)
	{
	   clear();
	
	   // To translate a 4x4 matrix you must replace the bottom row values.  The first
	   // which is matrix[12] is for x, [13] is the y, and so on.  The last one is set to 1.0.
	   matrix[12] = translate.x;
	   matrix[13] = translate.y;
	   matrix[14] = translate.z;
	   matrix[15] = 1.0f;
	}
	
	
	public void translate(float x, float y, float z)
	{
	   clear();
	
	   // To translate a 4x4 matrix you must replace the bottom row values.  The first
	   // which is matrix[12] is for x, [13] is the y, and so on.  The last one is set to 1.0.
	   matrix[12] = x;
	   matrix[13] = y;
	   matrix[14] = z;
	   matrix[15] = 1.0f;
	}
	
	
	public void rotate(float angle, int x, int y, int z)
	{
	   float sine = (float)Math.sin(PI_VALUE * angle / 180);
	   float cosine = (float)Math.cos(PI_VALUE * angle / 180);
	
	   if(x >= 1)
	      {
	              matrix[5] = cosine;
	              matrix[6] = sine;
	              matrix[9] = -sine;
	              matrix[10] = cosine;
	      }
	
	   if(y >= 1)
	      {
	              matrix[0] = cosine;
	              matrix[2] = -sine;
	              matrix[8] = sine;
	              matrix[10] = cosine;
	      }
	
	   if(z >= 1)
	      {
	              matrix[0] = cosine;
	              matrix[1] = sine;
	              matrix[4] = -sine;
	              matrix[5] = cosine;
	      }
	}
	
	
	
	public boolean inverseMatrix(Matrix4f m)
	{
	   float[] tempMatrix = new float[16];
	         
	   float d12, d13, d23, d24, d34, d41;
	
	        d12 = m.matrix[2]  * m.matrix[7]  - m.matrix[3]  * m.matrix[6];
	        d13 = m.matrix[2]  * m.matrix[11] - m.matrix[3]  * m.matrix[10];
	        d23 = m.matrix[6]  * m.matrix[11] - m.matrix[7]  * m.matrix[10];
	        d24 = m.matrix[6]  * m.matrix[15] - m.matrix[7]  * m.matrix[14];
	        d34 = m.matrix[10] * m.matrix[15] - m.matrix[11] * m.matrix[14];
	        d41 = m.matrix[14] * m.matrix[3]  - m.matrix[15] * m.matrix[2];
	
	        tempMatrix[0] =   m.matrix[5] * d34 - m.matrix[9] * d24 + m.matrix[13] * d23;
	        tempMatrix[1] = -(m.matrix[1] * d34 + m.matrix[9] * d41 + m.matrix[13] * d13);
	        tempMatrix[2] =   m.matrix[1] * d24 + m.matrix[5] * d41 + m.matrix[13] * d12;
	        tempMatrix[3] = -(m.matrix[1] * d23 - m.matrix[5] * d13 + m.matrix[9]  * d12);
	
	        // Calculate the determinant.
	        float determinant = m.matrix[0] * tempMatrix[0] + m.matrix[4] * tempMatrix[1] +
	                            m.matrix[8] * tempMatrix[2] + m.matrix[12] * tempMatrix[3];
	
	        // Clear if the determinant is equal to zero.  0 means matrix have no inverse.
	        if(determinant == 0.0)
	           {
	                   clear();
	         return false;
	           }
	
	        float invDeterminant = 1.0f / determinant;
	        
	        // Compute rest of inverse.
	        tempMatrix[0] *= invDeterminant;
	        tempMatrix[1] *= invDeterminant;
	        tempMatrix[2] *= invDeterminant;
	        tempMatrix[3] *= invDeterminant;
	
	        tempMatrix[4] = -(m.matrix[4] * d34 - m.matrix[8] * d24 + m.matrix[12] * d23) * invDeterminant;
	        tempMatrix[5] =   m.matrix[0] * d34 + m.matrix[8] * d41 + m.matrix[12] * d13  * invDeterminant;
	        tempMatrix[6] = -(m.matrix[0] * d24 + m.matrix[4] * d41 + m.matrix[12] * d12) * invDeterminant;
	        tempMatrix[7] =   m.matrix[0] * d23 - m.matrix[4] * d13 + m.matrix[8]  * d12  * invDeterminant;
	
	        // Pre-compute 2x2 dets for first two rows when computing cofactors 
	        // of last two rows.
	        d12 = m.matrix[0]  * m.matrix[5]  - m.matrix[1]  * m.matrix[12];
	        d13 = m.matrix[0]  * m.matrix[9]  - m.matrix[1]  * m.matrix[8];
	        d23 = m.matrix[4]  * m.matrix[9]  - m.matrix[5]  * m.matrix[8];
	        d24 = m.matrix[4]  * m.matrix[13] - m.matrix[5]  * m.matrix[12];
	        d34 = m.matrix[8]  * m.matrix[13] - m.matrix[9]  * m.matrix[12];
	        d41 = m.matrix[12] * m.matrix[1]  - m.matrix[13] * m.matrix[0];
	
	        tempMatrix[8]  =   m.matrix[7] * d34 - m.matrix[11] * d24 + m.matrix[15] * d23 * invDeterminant;
	        tempMatrix[9]  = -(m.matrix[3] * d34 + m.matrix[11] * d41 + m.matrix[15] * d13) * invDeterminant;
	        tempMatrix[10] =   m.matrix[3] * d24 + m.matrix[7]  * d41 + m.matrix[15] * d12 * invDeterminant;
	        tempMatrix[11] = -(m.matrix[3] * d23 - m.matrix[7]  * d13 + m.matrix[11] * d12) * invDeterminant;
	        tempMatrix[12] = -(m.matrix[6] * d34 - m.matrix[10] * d24 + m.matrix[14] * d23) * invDeterminant;
	        tempMatrix[13] =   m.matrix[2] * d34 + m.matrix[10] * d41 + m.matrix[14] * d13 * invDeterminant;
	        tempMatrix[14] = -(m.matrix[2] * d24 + m.matrix[6]  * d41 + m.matrix[14] * d12) * invDeterminant;
	        tempMatrix[15] =   m.matrix[2] * d23 - m.matrix[6]  * d13 + m.matrix[10] * d12 * invDeterminant;
	
	   // Save the temp matrix to our matrix.
	        matrix[0]  = tempMatrix[0];  matrix[1]  = tempMatrix[1];
	        matrix[2]  = tempMatrix[2];  matrix[3]  = tempMatrix[3];
	        matrix[4]  = tempMatrix[4];  matrix[5]  = tempMatrix[5];
	        matrix[6]  = tempMatrix[6];  matrix[7]  = tempMatrix[7];
	        matrix[8]  = tempMatrix[8];  matrix[9]  = tempMatrix[9];
	        matrix[10] = tempMatrix[10]; matrix[11] = tempMatrix[11];
	        matrix[12] = tempMatrix[12]; matrix[13] = tempMatrix[13];
	        matrix[14] = tempMatrix[14]; matrix[15] = tempMatrix[15];
	
	   return true;
	}
	
	public boolean invertMatrix(Matrix4f m)
	{
	   // Transpose rotation
	  matrix[ 0] = m.matrix[ 0];  matrix[ 1] = m.matrix[ 4];  matrix[ 2] = m.matrix[ 8];
	  matrix[ 4] = m.matrix[ 1];  matrix[ 5] = m.matrix[ 5];  matrix[ 6] = m.matrix[ 9];
	  matrix[ 8] = m.matrix[ 2];  matrix[ 9] = m.matrix[ 6];  matrix[10] = m.matrix[10];
	  
	  // Clear shearing terms
	  matrix[3] = 0.0f; matrix[7] = 0.0f; matrix[11] = 0.0f; matrix[15] = 1.0f;
	
	  // Translation is minus the dot of tranlation and rotations
	  matrix[12] = -(m.matrix[12]*m.matrix[0]) - (m.matrix[13]*m.matrix[1]) - (m.matrix[14]*m.matrix[ 2]);
	  matrix[13] = -(m.matrix[12]*m.matrix[4]) - (m.matrix[13]*m.matrix[5]) - (m.matrix[14]*m.matrix[ 6]);
	  matrix[14] = -(m.matrix[12]*m.matrix[8]) - (m.matrix[13]*m.matrix[9]) - (m.matrix[14]*m.matrix[10]);
	   
	  return true;
	}
	
	
	public Vector4f vectorMatrixMultiply(Vector4f v)
	{
	   Vector4f out = new Vector4f();
	
	   out.x = (v.x * matrix[0]) + (v.y * matrix[4]) + (v.z * matrix[8]) + matrix[12];
	   out.y = (v.x * matrix[1]) + (v.y * matrix[5]) + (v.z * matrix[9]) + matrix[13];
	   out.z = (v.x * matrix[2]) + (v.y * matrix[6]) + (v.z * matrix[10]) + matrix[14];
	
	   return out;
	}
	
	
	public Vector4f vectorMatrixMultiply3x3(Vector4f v)
	{
	   Vector4f out = new Vector4f();
	
	   out.x = (v.x * matrix[0]) + (v.y * matrix[4]) + (v.z * matrix[8]);
	   out.y = (v.x * matrix[1]) + (v.y * matrix[5]) + (v.z * matrix[9]);
	   out.z = (v.x * matrix[2]) + (v.y * matrix[6]) + (v.z * matrix[10]);
	
	   return out;
	}
	
	
	public Vector4f transformPoint(Vector4f v)
	{
	   float x = v.x;
	   float y = v.y;
	   float z = v.z;
	
	        v.x = x * matrix[0] + y * matrix[4] + z * matrix[8] + matrix[12];
	
	        v.y = x * matrix[1] + y * matrix[5] + z * matrix[9] + matrix[13];
	
	        v.z = x * matrix[2] + y * matrix[6] + z * matrix[10]+ matrix[14];
	
	        v.w = x * matrix[2] + y * matrix[7] + z * matrix[11]+ matrix[15];
	
	   return v;
	}
	
	
	public boolean createShadowMatrix(Vector4f planeNormal, Vector4f lightPos)
	{
	   clear();
	
	   // To create a shadow matrix we first need the dot product of the ground normal and
	   // the light position.  We store the result in a float called dotProduct.  We use the
	   // DotProduct function of our CVector4 class (doesn't this make the code look better?).
	   
	        float dotProduct = VectorMath.getDotProduct(planeNormal, lightPos);
	
	   // Create the shadow matrix by adding our values like so...
	        matrix[0]  = dotProduct - lightPos.x * planeNormal.x;
	        matrix[4]  = 0.0f       - lightPos.x * planeNormal.y;
	        matrix[8]  = 0.0f       - lightPos.x * planeNormal.z;
	        matrix[12] = 0.0f       - lightPos.x * planeNormal.w;
	
	        matrix[1]  = 0.0f       - lightPos.y * planeNormal.x;
	        matrix[5]  = dotProduct - lightPos.y * planeNormal.y;
	        matrix[9]  = 0.0f       - lightPos.y * planeNormal.z;
	        matrix[13] = 0.0f       - lightPos.y * planeNormal.w;
	
	        matrix[2]  = 0.0f       - lightPos.z * planeNormal.x;
	        matrix[6]  = 0.0f       - lightPos.z * planeNormal.y;
	        matrix[10] = dotProduct - lightPos.z * planeNormal.z;
	        matrix[14] = 0.0f       - lightPos.z * planeNormal.w;
	
	        matrix[3]  = 0.0f       - lightPos.w * planeNormal.x;
	        matrix[7]  = 0.0f       - lightPos.w * planeNormal.y;
	        matrix[11] = 0.0f       - lightPos.w * planeNormal.z;
	        matrix[15] = dotProduct - lightPos.w * planeNormal.w;
	
	   // Notice that the dotProuct is being subracted from the light position and the plane
	   // normal diagonally.  You will also notice that the x light position is used in the
	   // first column, y for the second, and so on.  This is the math to create a shadow
	   // projection matrix.  I wouldn't both taking out a pencil and paper and tracing this
	   // on out.  Just know that this is how you set it up.
	
	   return true;
	}
	
	public String toString() {
	        return "|" + matrix[0] + " "  + matrix[1] + " "  + matrix[2] + " "  + matrix[3] + "|";
	         
	}
}
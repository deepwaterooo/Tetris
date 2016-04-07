package dev.anogl;

//import dev.anogl.Attribute;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Map;
//import dev.anogl.utils.TangentsGenerator;

public final class Mesh {
    //private EnumMap<Attribute, Integer> attribPosMap;

    private int[] attribPositions;
    private int batchSize;

    private ShortBuffer indices;
    private int indicesBatchLength;
    private int indicesLength;
    private short[] indicesData;

    private FloatBuffer vertices;
    private int verticesBatchCount;
    private int verticesCount;
    private float[] verticesData;

    private int verticesStride;
    private int verticesStrideInBytes;
    /*
    public Mesh(float[] paramArrayOfFloat, int paramInt, EnumMap<Attribute, Integer> paramEnumMap) {
        this(paramArrayOfFloat, paramInt, paramEnumMap, 0);
    }
    */
    
    // EnumMap<Attribute, Integer> paramEnumMap
    //public Mesh(float[] paramArrayOfFloat, int paramInt1, EnumMap<Attribute, Integer> paramEnumMap, int paramInt2) {
    public Mesh(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
        if (paramInt1 < 1)
            throw new IllegalArgumentException("Vertices stride can not be less than 1");
        if (paramInt2 < 0)
            throw new IllegalArgumentException("Batch size can not be less than 0");
        this.verticesCount = (paramArrayOfFloat.length / paramInt1); // paramInt1 ?
        int i;
        int j;
        float[] arrayOfFloat;
        int k;
        Iterator localIterator = null;
        if (paramInt2 > 0) {
            this.verticesBatchCount = this.verticesCount; // the value is ?
            this.verticesCount = (paramInt2 * this.verticesBatchCount);
            i = paramInt1 + 1;
            j = i * this.verticesBatchCount;
            arrayOfFloat = new float[paramInt2 * j];
            k = 0;
            if (k >= paramInt2) {
                //paramEnumMap.put((Attribute)BATCHPOS, Integer.valueOf(paramInt1));
                paramArrayOfFloat = arrayOfFloat;
                paramInt1 = i;
            }
        } else {
            this.vertices = ByteBuffer.allocateDirect(4 * paramArrayOfFloat.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
            this.vertices.put(paramArrayOfFloat).position(0);
            this.verticesStrideInBytes = (paramInt1 * 4);
            this.batchSize = paramInt2;
            //this.attribPositions = new int[Attribute.values().length];
            Arrays.fill(this.attribPositions, -1);
            //localIterator = paramEnumMap.entrySet().iterator();
        }
        while (true) { /*
            if (!localIterator.rlocalIteratorhasNext()) {
                this.verticesData = paramArrayOfFloat;
                this.verticesStride = paramInt1;
                //this.attribPosMap = paramEnumMap;
                return;
                for (int m = 0; ; m++) {
                    if (m >= this.verticesBatchCount)
                        {
                            k++;
                            break;
                        }
                    int n = k * j + m * i;
                    System.arraycopy(paramArrayOfFloat, m * paramInt1, arrayOfFloat, n, paramInt1);
                    arrayOfFloat[(n + paramInt1)] = k;
                }
                } */
            Map.Entry localEntry = (Map.Entry)localIterator.next();
            //this.attribPositions[((Attribute)localEntry.getKey()).ordinal()] = ((Integer)localEntry.getValue()).intValue();
        }
    }
    /*
    public Mesh(float[] paramArrayOfFloat, short[] paramArrayOfShort, int paramInt, EnumMap<Attribute, Integer> paramEnumMap) {
        this(paramArrayOfFloat, paramArrayOfShort, paramInt, paramEnumMap, 0);
    }
    */
    //public Mesh(float[] paramArrayOfFloat, short[] paramArrayOfShort, int paramInt1, EnumMap<Attribute, Integer> paramEnumMap, int paramInt2) {
    public Mesh(float[] paramArrayOfFloat, short[] paramArrayOfShort, int paramInt1, int paramInt2) {
        //this(paramArrayOfFloat, paramInt1, paramEnumMap, paramInt2);
        short[] arrayOfShort;
        int i;
        int j;
        if (paramInt2 > 0) {
            if (-1 + this.verticesCount > 32767)
                throw new RuntimeException("Cloned indices are out of range");
            this.indicesBatchLength = paramArrayOfShort.length;
            arrayOfShort = new short[paramInt2 * this.indicesBatchLength];
            i = 0;
            j = 0;
            if (j >= paramInt2)
                paramArrayOfShort = arrayOfShort;
        } else {
            this.indices = ByteBuffer.allocateDirect(2 * paramArrayOfShort.length).order(ByteOrder.nativeOrder()).asShortBuffer();
            this.indices.put(paramArrayOfShort).position(0);
            this.indicesLength = paramArrayOfShort.length;
            this.indicesData = paramArrayOfShort;
            return;
        } 
        for (int k = 0; ; k++) {
            if (k >= this.indicesBatchLength) {
                i += this.verticesBatchCount;
                j++;
                break;
            }
            arrayOfShort[(k + j * this.indicesBatchLength)] = ((short)(i + paramArrayOfShort[k]));
        }
    }
    
    /*
    public void bindAttribute(Attribute paramAttribute, int paramInt) {
        int i = this.attribPositions[paramAttribute.ordinal()];
        if (i < 0)
            throw new RuntimeException("No such attribute: " + paramAttribute);
        this.vertices.position(i);
        GLES20.glVertexAttribPointer(paramInt, paramAttribute.size, 5126, false, this.verticesStrideInBytes, this.vertices);
    }

    public Mesh createBatchMesh(int paramInt) {
        if (this.indicesData != null)
            return new Mesh(this.verticesData, this.indicesData, this.verticesStride, this.attribPosMap, paramInt);
        return new Mesh(this.verticesData, this.verticesStride, this.attribPosMap, paramInt);
    }
    */
    public Mesh createMeshWithTangents() {
        //if (this.attribPosMap.containsKey(Attribute.TANGENT))
        //return this;
        int i = 3 + this.verticesStride;
        //float[] arrayOfFloat1 = TangentsGenerator.generateTangents(this.verticesData, this.indicesData, this.verticesStride, this.attribPosMap);
        float[] arrayOfFloat2 = new float[i * this.verticesCount];
        EnumMap localEnumMap;
        for (int j = 0; ; j++) {
            if (j >= this.verticesCount) {
                //localEnumMap = new EnumMap(this.attribPosMap);
                //localEnumMap.put(Attribute.TANGENT, Integer.valueOf(this.verticesStride));
                if (this.indicesData == null)
                    break;
                //return new Mesh(arrayOfFloat2, this.indicesData, i, localEnumMap, this.batchSize);
                return new Mesh(arrayOfFloat2, this.indicesData, i, this.batchSize);
            }
            System.arraycopy(this.verticesData, j * this.verticesStride, arrayOfFloat2, j * i, this.verticesStride);
            int k = j * i + this.verticesStride;
            int m = j * 3;
            int n = k + 1;
            int i1 = m + 1;
            //arrayOfFloat2[k] = arrayOfFloat1[m];
            int i2 = n + 1;
            int i3 = i1 + 1;
            //arrayOfFloat2[n] = arrayOfFloat1[i1];
            //(i2 + 1);
            //(i3 + 1);
            i2++;
            i3++;
            //arrayOfFloat2[i2] = arrayOfFloat1[i3];
        }
        //return new Mesh(arrayOfFloat2, i, localEnumMap, this.batchSize);
        return new Mesh(arrayOfFloat2, i, this.batchSize);
    }

    public void draw() { draw(0); }

    public void draw(int paramInt) {
        if (paramInt < 0)
            throw new IllegalArgumentException("Batch count can not be less than 0");
        if ((this.batchSize > 0) && (paramInt == 0))
            throw new RuntimeException("Mesh uses batching, so batch count must be greater than 0");
        if ((paramInt > 0) && (this.batchSize == 0))
            throw new RuntimeException("Mesh does not use batching, so batch count must be equal 0");
        if (paramInt > this.batchSize)
            throw new RuntimeException("Batch count can not be greater than " + this.batchSize);
        if (this.indices != null) {
            if (paramInt > 0) {
                GLES20.glDrawElements(4, paramInt * this.indicesBatchLength, 5123, this.indices);
                return;
            }
            GLES20.glDrawElements(4, this.indicesLength, 5123, this.indices);
            return;
        }
        if (paramInt > 0) {
            GLES20.glDrawArrays(4, 0, paramInt * this.verticesBatchCount);
            return;
        }
        GLES20.glDrawArrays(4, 0, this.verticesCount);
    }
}

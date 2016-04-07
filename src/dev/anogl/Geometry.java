package dev.anogl;

import android.opengl.Matrix;

public class Geometry {
    protected static final float[] IDENTITY_WORLD_MATRIX = new float[16];
    protected boolean enabled = true;
    //protected Material material;
    protected Mesh mesh;
    Node parent;
    protected int renderQueueId;
    protected float[] worldMatrix = IDENTITY_WORLD_MATRIX;
    static {
        Matrix.setIdentityM(IDENTITY_WORLD_MATRIX, 0);
    }

    public Geometry() { }

    //public Geometry(Mesh paramMesh, Material paramMaterial) {
    public Geometry(Mesh paramMesh) {
        this.mesh = paramMesh;
        //this.material = paramMaterial;
    }

    public boolean isEnabled() { return this.enabled; }
    public Mesh getMesh() { return this.mesh; }
    public int getRenderQueueId() { return this.renderQueueId; }
    public float[] getWorldMatrix() { return this.worldMatrix; }
    public void setEnabled(boolean paramBoolean) { this.enabled = paramBoolean; }
    public void setMesh(Mesh paramMesh) { this.mesh = paramMesh; }
    public void setRenderQueueId(int paramInt) { this.renderQueueId = paramInt; }
    public void setWorldMatrix(float[] paramArrayOfFloat) { this.worldMatrix = paramArrayOfFloat; }
    public void update(float paramFloat) { }

    //public void setMaterial(Material paramMaterial) { this.material = paramMaterial; }
    //public Material getMaterial() { return this.material; }
    /*
    public void render(RenderParams paramRenderParams) {
        this.material.render(this.mesh, this.worldMatrix, paramRenderParams);
        }*/
}

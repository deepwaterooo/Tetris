package dev.ttetris.util;

import android.opengl.Matrix;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dev.ttetris.util.Quaternion;
import dev.ttetris.util.Vector3;

public class Node {
    private static final float ROT_EPS = 1.0E-06F;
    private List<Node> children = new ArrayList();
    private List<Geometry> geometries = new ArrayList();
    private Node parent = null;
    private float x = 0.0F;
    private float y = 0.0F;
    private float z = 0.0F;
    
    private float rx = 0.0F;
    private float ry = 0.0F;
    private float rz = 0.0F;
    
    private float sx = 1.0F;
    private float sy = 1.0F;
    private float sz = 1.0F;
    
    private float[] worldMatrix = new float[16];
    private float[] localMatrix = new float[16];
    private boolean dirty = true;
    protected boolean enabled = true;
    private Quaternion rotation;

    public boolean addChild(Node paramNode) {
        if (this.children.contains(paramNode))
            return false;
        if (paramNode.parent != null)
            paramNode.parent.removeChild(paramNode);
        this.children.add(paramNode);
        paramNode.parent = this;
        return true;
    }

    public boolean attachGeometry(Geometry paramGeometry) {
        if ((paramGeometry.parent != null) && (!paramGeometry.parent.detachGeometry(paramGeometry)));
        while (!this.geometries.add(paramGeometry))
            return false;
        paramGeometry.parent = this;
        return true;
    }

    public boolean detachGeometry(Geometry paramGeometry) {
        if (this.geometries.remove(paramGeometry)) {
            paramGeometry.parent = null;
            return true;
        }
        return false;
    }

    public float getX() { return this.x; }
    public float getY() { return this.y; }
    public float getZ() { return this.z; }
    
    public float getScaleX() { return this.sx; }
    public float getScaleY() { return this.sy; }
    public float getScaleZ() { return this.sz; }
    
    public float getRotX() { return this.rx; }
    public float getRotY() { return this.ry; }
    public float getRotZ() { return this.rz; }
    
    public Quaternion getRotation() { return this.rotation; }
    public boolean isEnabled() { return this.enabled; }
    public List<Node> getChildren() { return this.children; }
    public List<Geometry> getGeometries() { return this.geometries; }
    public Node getParent() { return this.parent; }
    public Vector3 getPosition() { return new Vector3(this.x, this.y, this.z); }

    public boolean removeChild(Node paramNode) {
        if (this.children.remove(paramNode)) {
            paramNode.parent = null;
            return true;
        }
        return false;
    }

    public boolean removeFromParent() {
        if (this.parent != null)
            return this.parent.removeChild(this);
        return false;
    }

    public void setEnabled(boolean paramBoolean) {
        this.enabled = paramBoolean;
    }

    public void setPosition(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.x = paramFloat1;
        this.y = paramFloat2;
        this.z = paramFloat3;
        this.dirty = true;
    }

    public void setPosition(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        if ((Math.abs(this.x - paramFloat1) > paramFloat4) || (Math.abs(this.y - paramFloat2) > paramFloat4) || (Math.abs(this.z - paramFloat3) > paramFloat4)) {
            this.x = paramFloat1;
            this.y = paramFloat2;
            this.z = paramFloat3;
            this.dirty = true;
        }
    }

    public void setPosition(Vector3 paramVector3) {
        setPosition(paramVector3.x, paramVector3.y, paramVector3.z); }
    public void setPosition(Vector3 paramVector3, float paramFloat) {
        setPosition(paramVector3.x, paramVector3.y, paramVector3.z, paramFloat); }

    public void setRotation(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.rx = paramFloat1;
        this.ry = paramFloat2;
        this.rz = paramFloat3;
        this.rotation = null;
        this.dirty = true;
    }

    public void setRotation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        if ((Math.abs(this.rx - paramFloat1) > paramFloat4) || (Math.abs(this.ry - paramFloat2) > paramFloat4) || (Math.abs(this.rz - paramFloat3) > paramFloat4)) {
            this.rx = paramFloat1;
            this.ry = paramFloat2;
            this.rz = paramFloat3;
            this.rotation = null;
            this.dirty = true;
        }
    }

    public void setRotation(Quaternion paramQuaternion) {
        this.rotation = paramQuaternion;
        this.dirty = true;
    }

    public void setRotation(Quaternion paramQuaternion, float paramFloat) {
        if ((Math.abs(this.rotation.w - paramQuaternion.w) > paramFloat) || (Math.abs(this.rotation.x - paramQuaternion.x) > paramFloat)
            || (Math.abs(this.rotation.y - paramQuaternion.y) > paramFloat) || (Math.abs(this.rotation.z - paramQuaternion.z) > paramFloat)) {
            this.rotation = paramQuaternion;
            this.dirty = true;
        }
    }

    public void setScale(float paramFloat) {
        setScale(paramFloat, paramFloat, paramFloat); }
    public void setScale(float paramFloat1, float paramFloat2) {
        setScale(paramFloat1, paramFloat1, paramFloat1, paramFloat2); }

    public void setScale(float paramFloat1, float paramFloat2, float paramFloat3) {
        this.sx = paramFloat1;
        this.sy = paramFloat2;
        this.sz = paramFloat3;
        this.dirty = true;
    }

    public void setScale(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        if ((Math.abs(this.sx - paramFloat1) > paramFloat4) || (Math.abs(this.sy - paramFloat2) > paramFloat4) || (Math.abs(this.sz - paramFloat3) > paramFloat4)) {
            this.sx = paramFloat1;
            this.sy = paramFloat2;
            this.sz = paramFloat3;
            this.dirty = true;
        }
    }

    public void traverseSceneGraph(List<Geometry> paramList, float[] paramArrayOfFloat) {
        if (!this.enabled)
            return;
        //label83: Iterator localIterator1;
        if (this.dirty) {
            Matrix.setIdentityM(this.localMatrix, 0);
            Matrix.translateM(this.localMatrix, 0, this.x, this.y, this.z);
            if (this.rotation != null) {
                float[] arrayOfFloat = this.rotation.toAngleAxis();
                Matrix.rotateM(this.localMatrix, 0, arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
                Matrix.scaleM(this.localMatrix, 0, this.sx, this.sy, this.sz);
                this.dirty = false;
            }
        } else {
            Matrix.multiplyMM(this.worldMatrix, 0, paramArrayOfFloat, 0, this.localMatrix, 0);
            //localIterator1 = this.geometries.iterator();
        }
        while (true) { /*
            if (!localIterator1.hasNext()) {
                Iterator localIterator2 = this.children.iterator();
                while (localIterator2.hasNext())
                    ((Node)localIterator2.next()).traverseSceneGraph(paramList, this.worldMatrix);
                break;
                if (Math.abs(this.rx) > 1.0E-06F)
                    Matrix.rotateM(this.localMatrix, 0, this.rx, 1.0F, 0.0F, 0.0F);
                if (Math.abs(this.ry) > 1.0E-06F)
                    Matrix.rotateM(this.localMatrix, 0, this.ry, 0.0F, 1.0F, 0.0F);
                if (Math.abs(this.rz) <= 1.0E-06F)
                    break label83;
                Matrix.rotateM(this.localMatrix, 0, this.rz, 0.0F, 0.0F, 1.0F);
                break label83;
            }
            Geometry localGeometry = (Geometry)localIterator1.next();  
            if (localGeometry.enabled) {
                localGeometry.setWorldMatrix(this.worldMatrix);
                paramList.add(localGeometry);
                } */
        }
    }

    public void update(float paramFloat) {
        Iterator localIterator1 = this.geometries.iterator();
        Iterator localIterator2;
        if (!localIterator1.hasNext())
            localIterator2 = this.children.iterator();
        while (true) { /*
            if (!localIterator2.hasNext()) {
                return;
                Geometry localGeometry = (Geometry)localIterator1.next();
                if (!localGeometry.enabled)
                    break;
                localGeometry.update(paramFloat);
                break;
                } 
            Node localNode = (Node)localIterator2.next();
            if (localNode.enabled)
            localNode.update(paramFloat);  */
        }
    }
}

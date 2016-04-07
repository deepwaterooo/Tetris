package dev.anogl.utils;

import java.io.IOException;
import java.io.InputStream;
import dev.anogl.Mesh;

public abstract interface MeshLoader {
  public abstract Mesh loadMesh(InputStream paramInputStream)
    throws IOException;
}

package dev.ttetris.util;

import java.io.IOException;
import java.io.InputStream;
import dev.ttetris.util.Mesh;

public abstract interface MeshLoader {
  public abstract Mesh loadMesh(InputStream paramInputStream)
    throws IOException;
}

package dev.anogl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import dev.anogl.Attribute;
import dev.anogl.Mesh;

public class MeshPlyLoader implements MeshLoader {
    private VertexSpec getVertexSpec(ElementSpec paramElementSpec) {
        VertexSpec localVertexSpec = new VertexSpec(null);
        int i = paramElementSpec.properties.size();
        localVertexSpec.verticesDataMap = new int[i];
        Arrays.fill(localVertexSpec.verticesDataMap, -1);
        int j = 0;
        if (j >= i)
            return localVertexSpec;
        PropertySpec localPropertySpec1 = (PropertySpec)paramElementSpec.properties.get(j);
        if (localPropertySpec1.name.equals("x")) {
            PropertySpec localPropertySpec4 = (PropertySpec)paramElementSpec.properties.get(j + 1);
            PropertySpec localPropertySpec5 = (PropertySpec)paramElementSpec.properties.get(j + 2);
            if ((localPropertySpec4.name.equals("y")) && (localPropertySpec5.name.equals("z"))) {
                int n = localVertexSpec.verticesStride;
                localVertexSpec.attribPosMap.put(Attribute.POSITION, Integer.valueOf(n));
                localVertexSpec.verticesDataMap[j] = n;
                localVertexSpec.verticesDataMap[(j + 1)] = (n + 1);
                localVertexSpec.verticesDataMap[(j + 2)] = (n + 2);
                localVertexSpec.verticesStride = (3 + localVertexSpec.verticesStride);
            }
        }
        while (true) {
            j++;
            break;
            throw new RuntimeException("Inconsistent position attribute");
            if (localPropertySpec1.name.equals("nx")) {
                PropertySpec localPropertySpec2 = (PropertySpec)paramElementSpec.properties.get(j + 1);
                PropertySpec localPropertySpec3 = (PropertySpec)paramElementSpec.properties.get(j + 2);
                if ((localPropertySpec2.name.equals("ny")) && (localPropertySpec3.name.equals("nz"))) {
                    int m = localVertexSpec.verticesStride;
                    localVertexSpec.attribPosMap.put(Attribute.NORMAL, Integer.valueOf(m));
                    localVertexSpec.verticesDataMap[j] = m;
                    localVertexSpec.verticesDataMap[(j + 1)] = (m + 1);
                    localVertexSpec.verticesDataMap[(j + 2)] = (m + 2);
                    localVertexSpec.verticesStride = (3 + localVertexSpec.verticesStride);
                }
                else {
                    throw new RuntimeException("Inconsistent normal attribute");
                }
            }
            else if (localPropertySpec1.name.equals("s")) {
                if (!((PropertySpec)paramElementSpec.properties.get(j + 1)).name.equals("t"))
                    break label472;
                int k = localVertexSpec.verticesStride;
                localVertexSpec.attribPosMap.put(Attribute.TEXCOORD, Integer.valueOf(k));
                localVertexSpec.verticesDataMap[j] = k;
                localVertexSpec.verticesDataMap[(j + 1)] = (k + 1);
                localVertexSpec.verticesStride = (2 + localVertexSpec.verticesStride);
            }
        }
        label472: throw new RuntimeException("Inconsistent texcoord attribute");
    }

    private void passElementData(ElementSpec paramElementSpec, BufferedReader paramBufferedReader)
        throws IOException {
        int i = paramElementSpec.count;
        for (int j = 0; ; j++) {
            if (j >= i)
                return;
            if (paramBufferedReader.readLine() == null)
                throw new RuntimeException("Invalid element data");
        }
    }

    private short[] readIndicesData(ElementSpec paramElementSpec, BufferedReader paramBufferedReader)
        throws IOException {
        int i = paramElementSpec.count;
        ArrayList localArrayList = new ArrayList();
        int j = 0;
        short[] arrayOfShort;
        if (j >= i)
            arrayOfShort = new short[localArrayList.size()];
        for (int i3 = 0; ; i3++) {
            if (i3 >= arrayOfShort.length) {
                return arrayOfShort;
                String str = paramBufferedReader.readLine();
                if (str == null)
                    throw new RuntimeException("Invalid indices data");
                String[] arrayOfString = str.split("[ \\r\\n]+");
                int k = Integer.parseInt(arrayOfString[0]);
                if ((k != 3) && (k != 4))
                    throw new RuntimeException("Unsupported face size");
                int m = Integer.parseInt(arrayOfString[1]);
                int n = Integer.parseInt(arrayOfString[2]);
                int i1 = Integer.parseInt(arrayOfString[3]);
                if ((m > 32767) || (n > 32767) || (i1 > 32767))
                    throw new RuntimeException("Only 16-bit indices supported");
                localArrayList.add(Short.valueOf((short)m));
                localArrayList.add(Short.valueOf((short)n));
                localArrayList.add(Short.valueOf((short)i1));
                if (k == 4) {
                    int i2 = Integer.parseInt(arrayOfString[4]);
                    if (i2 > 32767)
                        throw new RuntimeException("Only 16-bit indices supported");
                    localArrayList.add(Short.valueOf((short)m));
                    localArrayList.add(Short.valueOf((short)i1));
                    localArrayList.add(Short.valueOf((short)i2));
                }
                j++;
                break;
            }
            arrayOfShort[i3] = ((Short)localArrayList.get(i3)).shortValue();
        }
    }

    private float[] readVerticesData(ElementSpec paramElementSpec, VertexSpec paramVertexSpec, BufferedReader paramBufferedReader)
        throws IOException {
        int i = paramElementSpec.count;
        int j = paramElementSpec.properties.size();
        float[] arrayOfFloat = new float[i * paramVertexSpec.verticesStride];
        int k = 0;
        int m = 0;
        String[] arrayOfString;
        int n;
        int i1;
        while (true) {
            if (m >= i)
                return arrayOfFloat;
            String str = paramBufferedReader.readLine();
            if (str == null)
                throw new RuntimeException("Invalid vertices data");
            arrayOfString = str.split("[ \\r\\n]+");
            n = 0;
            i1 = k;
            if (n < j)
                break;
            m++;
            k = i1;
        }
        int i2 = paramVertexSpec.verticesDataMap[n];
        int i3;
        if (i2 != -1) {
            i3 = i1 + 1;
            arrayOfFloat[i1] = Float.parseFloat(arrayOfString[i2]);
        }
        while (true) {
            n++;
            i1 = i3;
            break;
            i3 = i1;
        }
    }

    public Mesh loadMesh(InputStream paramInputStream)
        throws IOException {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
        if (!localBufferedReader.readLine().startsWith("ply"))
            throw new RuntimeException("Invalid signature line");
        ArrayList localArrayList = new ArrayList();
        String[] arrayOfString;
        do {
            String str = localBufferedReader.readLine();
            if (str == null)
                throw new RuntimeException("Invalid file format");
            arrayOfString = str.split("[ \\r\\n]+");
        }
        while (arrayOfString.length == 0);
        int i;
        EnumMap localEnumMap;
        float[] arrayOfFloat;
        short[] arrayOfShort;
        Iterator localIterator;
        if (arrayOfString[0].equals("end_header")) {
            i = 0;
            localEnumMap = null;
            arrayOfFloat = (float[])null;
            arrayOfShort = (short[])null;
            localIterator = localArrayList.iterator();
        }
        while (true) {
            if (!localIterator.hasNext()) {
                if ((arrayOfFloat != null) && (localEnumMap != null) && (i != 0))
                    break label556;
                throw new RuntimeException("Invalid vertices data");
                if ((arrayOfString[0].equals("comment")) || (arrayOfString[0].equals("obj_info")))
                    break;
                if (arrayOfString[0].equals("format")) {
                    if (arrayOfString.length < 3)
                        throw new RuntimeException("Invalid format line");
                    if (!arrayOfString[1].equals("ascii"))
                        throw new RuntimeException("Unsupported file format");
                    if (arrayOfString[2].equals("1.0"))
                        break;
                    throw new RuntimeException("Unknown file version");
                }
                if (arrayOfString[0].equals("element")) {
                    if (arrayOfString.length < 3)
                        throw new RuntimeException("Invalid element line");
                    ElementSpec localElementSpec1 = new ElementSpec(null);
                    localElementSpec1.name = arrayOfString[1];
                    localElementSpec1.count = Integer.parseInt(arrayOfString[2]);
                    localArrayList.add(localElementSpec1);
                    break;
                }
                if (!arrayOfString[0].equals("property"))
                    break;
                if (localArrayList.isEmpty())
                    throw new RuntimeException("Property without element");
                PropertySpec localPropertySpec = new PropertySpec(null);
                if (arrayOfString[1].equals("list")) {
                    localPropertySpec.name = arrayOfString[4];
                    localPropertySpec.listType = arrayOfString[2];
                }
                for (localPropertySpec.numericType = arrayOfString[3]; ; localPropertySpec.numericType = arrayOfString[1]) {
                    ((ElementSpec)localArrayList.get(-1 + localArrayList.size())).properties.add(localPropertySpec);
                    break;
                    localPropertySpec.name = arrayOfString[2];
                }
            }
            ElementSpec localElementSpec2 = (ElementSpec)localIterator.next();
            if (localElementSpec2.name.equals("vertex")) {
                VertexSpec localVertexSpec = getVertexSpec(localElementSpec2);
                arrayOfFloat = readVerticesData(localElementSpec2, localVertexSpec, localBufferedReader);
                i = localVertexSpec.verticesStride;
                localEnumMap = localVertexSpec.attribPosMap;
            }
            else if (localElementSpec2.name.equals("face")) {
                arrayOfShort = readIndicesData(localElementSpec2, localBufferedReader);
            }
            else {
                passElementData(localElementSpec2, localBufferedReader);
            }
        }
        label556: if (arrayOfShort != null)
            return new Mesh(arrayOfFloat, arrayOfShort, i, localEnumMap);
        return new Mesh(arrayOfFloat, i, localEnumMap);
    }

    private class ElementSpec {
        int count;
        String name;
        List<MeshPlyLoader.PropertySpec> properties = new ArrayList();

        private ElementSpec() {
        }
    }

    private class PropertySpec {
        String listType;
        String name;
        String numericType;

        private PropertySpec() {
        }
    }

    private class VertexSpec {
        EnumMap<Attribute, Integer> attribPosMap = new EnumMap(Attribute.class);
        int[] verticesDataMap;
        int verticesStride;

        private VertexSpec() {
        }
    }
}

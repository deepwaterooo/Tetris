package dev.ttetris.util;

import java.util.Comparator;

public class GeometryComparator implements Comparator<Geometry> {
    public int compare(Geometry paramGeometry1, Geometry paramGeometry2) {
        int i = paramGeometry1.renderQueueId - paramGeometry2.renderQueueId;
        if (i != 0)
            return i;
        /*
        boolean bool = paramGeometry1.material.isTransparent();
        if (bool != paramGeometry2.material.isTransparent()) {
            if (bool);
            for (int j = 1; ; j = -1)
                return j;
                } */
        return 0;
    }
}

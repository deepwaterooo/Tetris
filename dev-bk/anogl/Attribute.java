package dev.anogl;

public enum Attribute {
    public final int size;
    static {
        NORMAL = new Attribute("NORMAL", 1, 3);
        TANGENT = new Attribute("TANGENT", 2, 3);
        BATCHPOS = new Attribute("BATCHPOS", 3, 1);
        TEXCOORD = new Attribute("TEXCOORD", 4, 2);
        TEXCOORD01 = new Attribute("TEXCOORD01", 5, 4);
        TEXCOORD23 = new Attribute("TEXCOORD23", 6, 4);
        TEXCOORD45 = new Attribute("TEXCOORD45", 7, 4);
        TEXCOORD67 = new Attribute("TEXCOORD67", 8, 4);
        PARAMS0 = new Attribute("PARAMS0", 9, 4);
        PARAMS1 = new Attribute("PARAMS1", 10, 4);
        PARAMS2 = new Attribute("PARAMS2", 11, 4);
        PARAMS3 = new Attribute("PARAMS3", 12, 4);
        PARAMS4 = new Attribute("PARAMS4", 13, 4);
        PARAMS5 = new Attribute("PARAMS5", 14, 4);
        PARAMS6 = new Attribute("PARAMS6", 15, 4);
        PARAMS7 = new Attribute("PARAMS7", 16, 4);
        Attribute[] arrayOfAttribute = new Attribute[17];
        arrayOfAttribute[0] = POSITION;
        arrayOfAttribute[1] = NORMAL;
        arrayOfAttribute[2] = TANGENT;
        arrayOfAttribute[3] = BATCHPOS;
        arrayOfAttribute[4] = TEXCOORD;
        arrayOfAttribute[5] = TEXCOORD01;
        arrayOfAttribute[6] = TEXCOORD23;
        arrayOfAttribute[7] = TEXCOORD45;
        arrayOfAttribute[8] = TEXCOORD67;
        arrayOfAttribute[9] = PARAMS0;
        arrayOfAttribute[10] = PARAMS1;
        arrayOfAttribute[11] = PARAMS2;
        arrayOfAttribute[12] = PARAMS3;
        arrayOfAttribute[13] = PARAMS4;
        arrayOfAttribute[14] = PARAMS5;
        arrayOfAttribute[15] = PARAMS6;
        arrayOfAttribute[16] = PARAMS7;
    }
    private Attribute(int arg3) {
        int j;
        this.size = j;
    }
}

package dev.ttetris.util;

public enum Attribute {
    //static {
    POSITION("POSITION", 0, 0), // need double check, 
        NORMAL("NORMAL", 1, 3),
        TANGENT("TANGENT", 2, 3),
        BATCHPOS("BATCHPOS", 3, 1),
        TEXCOORD("TEXCOORD", 4, 2),
        TEXCOORD01("TEXCOORD01", 5, 4),
        TEXCOORD23("TEXCOORD23", 6, 4),
        TEXCOORD45("TEXCOORD45", 7, 4),
        TEXCOORD67("TEXCOORD67", 8, 4),
        PARAMS0("PARAMS0", 9, 4),
        PARAMS1("PARAMS1", 10, 4),
        PARAMS2("PARAMS2", 11, 4),
        PARAMS3("PARAMS3", 12, 4),
        PARAMS4("PARAMS4", 13, 4),
        PARAMS5("PARAMS5", 14, 4),
        PARAMS6("PARAMS6", 15, 4),
        PARAMS7("PARAMS7", 16, 4);
        public final String name;
        public final int i;
        public final int size;

        private Attribute(String arg1, int arg2, int arg3) {
            this.name = arg1;
            this.i = arg2;
            this.size = arg3;
        }
}

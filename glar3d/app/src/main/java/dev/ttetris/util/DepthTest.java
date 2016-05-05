package dev.ttetris.util;

public enum DepthTest {
        NONE("NONE", 0, 0),
        NEVER("NEVER", 1, 512),
        ALWAYS("ALWAYS", 2, 519),
        LESS("LESS", 3, 513),
        LEQUAL("LEQUAL", 4, 515),
        EQUAL("EQUAL", 5, 514),
        GREATER("GREATER", 6, 516),
        GEQUAL("GEQUAL", 7, 518),
        NOTEQUAL("NOTEQUAL", 8, 517);

        private String name;
        private int value;
        private int valueb;

        private DepthTest(String name, int arg2, int arg3) {
            this.name = name;
            this.value = arg2;
            this.valueb = arg3;
        }

        public int getValue() {
            return this.value;
        }
}

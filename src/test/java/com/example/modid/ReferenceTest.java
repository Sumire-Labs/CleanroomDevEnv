package com.example.modid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ReferenceTest {

    @Test
    void compiledConstantsContainResolvedTokens() {
        assertNotEquals("@" + "{MOD_ID}", Reference.MOD_ID);
        assertNotEquals("@" + "{MOD_VERSION}", Reference.VERSION);
        assertFalse(Reference.VERSION.isBlank());
    }

}

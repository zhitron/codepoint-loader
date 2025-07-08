package com.github.zhitron.codepoint_loader;

import org.junit.Test;

import java.io.File;

/**
 * @author zhitron
 */
public class CodepointLoaderTest {

    @Test
    public void test1() throws Exception {
        File file = new File("README.md");
        StringBuilder sb = new StringBuilder();
        try (CodepointLoader loader = CodepointLoaderFactory.of(file)) {
            while (loader.hasNextCodepoint()) {
                sb.appendCodePoint(loader.nextCodepoint());
            }
        }
        System.out.println(sb);
    }

    @Test
    public void test2() throws Exception {
        File file = new File("README.md");
        StringBuilder sb = new StringBuilder();
        try (CodepointLoader loader = CodepointLoaderFactory.of(file)) {
            int codepoint;
            while ((codepoint = loader.popCodepoint()) != -1) {
                sb.appendCodePoint(codepoint);
            }
        }
        System.out.println(sb);
    }
}

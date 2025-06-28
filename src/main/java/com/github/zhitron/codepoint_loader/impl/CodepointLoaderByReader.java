package com.github.zhitron.codepoint_loader.impl;

import com.github.zhitron.codepoint_loader.CodepointLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.CharBuffer;
import java.util.Objects;

/**
 * 该类通过 Reader 提供 Unicode Code Point 的加载功能。
 * 它使用 Reader 作为数据源，将字符数据读取到缓冲区中，并支持处理包括代理对在内的复杂 Unicode 字符。
 *
 * @author zhitron
 */
public class CodepointLoaderByReader extends CodepointLoader {
    /**
     * 数据源 Reader，用于读取字符数据。
     * 该 Reader 为 final 类型，确保其在对象生命周期内不可变。
     */
    private final Reader input;

    /**
     * 构造函数，初始化具有指定 Reader 和缓冲区大小的 CodepointLoaderByReader。
     *
     * @param input      提供字符数据的 Reader
     * @param bufferSize 缓冲区的大小
     */
    public CodepointLoaderByReader(Reader input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input, "The Reader cannot be null");
    }

    /**
     * 从 Reader 中加载字符数据到 CharBuffer 中。
     * 该方法由父类的 getCodepoint 方法触发，用于填充缓冲区以继续读取 Unicode Code Point。
     *
     * @param charBuffer 要填充的目标 CharBuffer
     */
    @Override
    protected void loadCharBuffer(CharBuffer charBuffer) {
        try {
            // 尝试从 Reader 读取数据到指定的 CharBuffer 中
            int len = input.read(charBuffer.array(), charBuffer.position(), charBuffer.remaining());

            // 如果没有更多字符可读，直接返回
            if (len <= 0) {
                return;
            }

            // 更新缓冲区位置，并准备读取已填充的数据
            charBuffer.position(charBuffer.position() + len);
        } catch (IOException e) {
            // 包装 IO 异常为 UncheckedIOException 并抛出
            throw new UncheckedIOException("Error to load char", e);
        }
    }

    /**
     * 关闭底层的 Reader 并释放相关资源。
     *
     * @throws Exception 如果关闭过程中发生错误，抛出此异常
     */
    @Override
    public void close() throws Exception {
        input.close();
    }
}
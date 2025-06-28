package com.github.zhitron.codepoint_loader.impl;

import com.github.zhitron.codepoint_loader.CodepointLoader;

import java.nio.CharBuffer;
import java.util.Objects;

/**
 * CodepointLoaderByCharBuffer 是一个基于 CharBuffer 的 Unicode Code Point 加载器实现类。
 * 该类通过封装一个只读的 CharBuffer 数据源，实现了从缓冲区中逐个读取 Unicode 字符（Code Point）的功能。
 * 它继承自 CodepointLoader 抽象类，并提供了具体的字符加载逻辑。
 *
 * @author zhitron
 */
public class CodepointLoaderByCharBuffer extends CodepointLoader {
    /**
     * 封装的字符数据源，用于提供 Unicode 字符数据。
     * 该缓冲区为 final 类型，确保其在对象生命周期内不可变。
     */
    private final CharBuffer input;

    /**
     * 构造函数，使用指定的 CharBuffer 和缓冲区大小初始化 CodepointLoaderByCharBuffer 实例。
     *
     * @param input      提供原始字符数据的 CharBuffer
     * @param bufferSize 缓冲区的大小，用于控制每次处理的数据量
     */
    public CodepointLoaderByCharBuffer(CharBuffer input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input, "The CharBuffer cannot be null");
    }

    /**
     * 将数据源中的字符加载到目标 CharBuffer 中。
     * 此方法负责从内部数据源 (this.data) 中复制字符到目标缓冲区 (charBuffer) 中，
     * 以供后续解析 Unicode Code Point 使用。
     *
     * @param charBuffer 要加载数据的目标 CharBuffer
     */
    @Override
    protected void loadCharBuffer(CharBuffer charBuffer) {
        // 计算可以复制的最大字符数，不超过两个缓冲区的剩余空间
        int len = Math.min(charBuffer.remaining(), input.remaining());

        // 如果没有更多字符可复制，则直接返回
        if (len <= 0) {
            return;
        }

        // 获取源缓冲区和目标缓冲区的底层字符数组
        char[] source = input.array();
        char[] target = charBuffer.array();

        // 获取当前数据源的位置作为复制的起始偏移量
        int offset = input.position();

        // 执行数组拷贝操作，将数据从源缓冲区复制到目标缓冲区
        System.arraycopy(source, offset, target, 0, len);

        // 更新数据源的当前位置，反映已复制的字符数量
        input.position(offset + len);
    }
}
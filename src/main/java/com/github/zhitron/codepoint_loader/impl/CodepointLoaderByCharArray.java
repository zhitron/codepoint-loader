package com.github.zhitron.codepoint_loader.impl;

import com.github.zhitron.codepoint_loader.CodepointLoader;

import java.nio.CharBuffer;
import java.util.Objects;

/**
 * 该类继承自CodepointLoader，用于从字符数组加载 Unicode Code Point。
 *
 * @author zhitron
 */
public class CodepointLoaderByCharArray extends CodepointLoader {
    /**
     * 存储要读取的字符数据的数组。
     * 该数组为 final 类型，确保其在对象生命周期内不可变。
     */
    private final char[] input;

    /**
     * 当前读取字符数组的位置偏移量。
     */
    private int offset = 0;

    /**
     * 构造函数，初始化字符数组和缓冲区大小。
     *
     * @param input      提供 Unicode 字符的数据源
     * @param bufferSize 缓冲区的大小
     */
    public CodepointLoaderByCharArray(char[] input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input, "The char[] cannot be null");
    }

    /**
     * 将字符数组中的数据加载到 CharBuffer 中。
     * 此方法会在需要更多字符时被调用，它会从当前 offset 开始填充 buffer。
     *
     * @param charBuffer 要填充数据的目标 CharBuffer
     */
    @Override
    protected void loadCharBuffer(CharBuffer charBuffer) {
        // 计算可读取的字符长度
        int len = Math.min(charBuffer.remaining(), input.length - offset);

        if (len <= 0) {
            return; // 没有剩余字符可加载
        }

        // 将字符放入缓冲区，并更新偏移量
        charBuffer.put(input, offset, len);
        offset += len;
    }

}
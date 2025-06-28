package com.github.zhitron.codepoint_loader.impl;

import com.github.zhitron.codepoint_loader.CodepointSequenceLoader;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * CodepointSequenceLoaderByByteArray 是一个具体的实现类，用于从字节数组加载 Unicode Code Point。
 * 它继承自抽象类 CodepointSequenceLoader，并实现了 loadData 方法以提供基于内存字节数组的读取逻辑。
 *
 * <p>此类适用于已将数据加载到内存中的场景，通过偏移量控制数据的逐步读取。</p>
 *
 * @author zhitron
 */
public class CodepointSequenceLoaderByByteArray extends CodepointSequenceLoader {
    /**
     * 存储原始字节数据的数组。
     * 该数组为 final 类型，确保其在对象生命周期内不可变。
     */
    private final byte[] input;

    /**
     * 当前读取位置的偏移量，用于跟踪已读取的数据位置。
     * 初始值为 0，表示从字节数组的起始位置开始读取。
     */
    private int offset = 0;

    /**
     * 构造函数，初始化具有指定字节数组、字符集和缓冲区大小的 CodepointSequenceLoaderByByteArray。
     *
     * @param input      要读取的原始字节数组，不能为 null
     * @param charset    用于解码字节数据的字符集，不能为 null
     * @param bufferSize 字符缓冲区的大小，必须大于 0
     */
    public CodepointSequenceLoaderByByteArray(byte[] input, Charset charset, int bufferSize) {
        super(charset, bufferSize);
        this.input = Objects.requireNonNull(input, "The byte[] cannot be null");
    }

    /**
     * 实现父类的 loadData 方法，从字节数组中读取数据并填充到目标缓冲区。
     *
     * <p>该方法根据当前偏移量计算可读取的字节长度，
     * 然后使用 System.arraycopy 将数据复制到目标缓冲区，
     * 并更新偏移量以准备下一次读取。</p>
     *
     * @param buffer 要填充数据的目标 byte 数组
     * @return 实际读取的字节数，如果到达字节数组末尾则返回 -1
     */
    @Override
    protected int loadData(byte[] buffer) {
        int len = Math.min(buffer.length, input.length - offset);
        if (len <= 0) {
            return -1; // 表示没有更多数据可读
        }

        System.arraycopy(input, offset, buffer, 0, len);
        offset += len;
        return len;
    }
}
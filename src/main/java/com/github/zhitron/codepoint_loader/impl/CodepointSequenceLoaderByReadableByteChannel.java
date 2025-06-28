package com.github.zhitron.codepoint_loader.impl;

import com.github.zhitron.codepoint_loader.CodepointSequenceLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * CodepointSequenceLoaderByReadableByteChannel 是 CodepointSequenceLoader 的具体实现类，
 * 它通过 ReadableByteChannel 读取字节数据，并将其解码为 Unicode Code Point。
 *
 * <p>此类使用 NIO 的 ReadableByteChannel 接口作为数据源，能够高效地从任意可读通道加载字节数据，
 * 并利用父类的字符集解码能力将字节流转换为字符流，最终提供 Unicode Code Point 序列。</p>
 *
 * @author zhitron
 */
public class CodepointSequenceLoaderByReadableByteChannel extends CodepointSequenceLoader {
    /**
     * 数据输入源，类型为 ReadableByteChannel，用于从底层数据源读取字节。
     * 该字段为 final 类型，确保其在对象生命周期内不可变。
     */
    private final ReadableByteChannel input;

    /**
     * 构造函数，创建一个基于 ReadableByteChannel 的 CodepointSequenceLoader。
     *
     * @param input      数据输入通道，不能为 null
     * @param charset    用于解码字节数据的字符集，不能为 null
     * @param bufferSize 字符缓冲区的大小，必须大于 0
     */
    public CodepointSequenceLoaderByReadableByteChannel(ReadableByteChannel input, Charset charset, int bufferSize) {
        super(charset, bufferSize);
        this.input = Objects.requireNonNull(input, "The ReadableByteChannel cannot be null");
    }

    /**
     * 实现父类的 loadData 方法，从 ReadableByteChannel 中读取字节数据填充到指定的 buffer 中。
     *
     * <p>该方法将给定的 byte 数组包装成 ByteBuffer，
     * 然后通过 ReadableByteChannel 的 read 方法读取数据并返回实际读取的字节数。</p>
     *
     * @param buffer 要填充数据的目标 byte 数组
     * @return 实际读取的字节数，如果到达数据源末尾则返回 0 或负值
     * @throws IOException 如果读取过程中发生 I/O 错误
     */
    @Override
    protected int loadData(byte[] buffer) throws IOException {
        return input.read(ByteBuffer.wrap(buffer));
    }

    /**
     * 关闭底层的数据输入通道。
     *
     * <p>此方法实现了 AutoCloseable 接口的 close 方法，
     * 用于释放与输入通道相关的资源。</p>
     *
     * @throws IOException 如果关闭过程中发生 I/O 错误
     */
    @Override
    public void close() throws IOException {
        input.close();
    }
}
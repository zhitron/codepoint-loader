package com.github.zhitron.codepoint_loader.impl;

import com.github.zhitron.codepoint_loader.CodepointSequenceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * CodepointSequenceLoaderByInputStream 是一个具体实现类，用于从 InputStream 加载 Unicode Code Point。
 * 它继承自抽象类 CodepointSequenceLoader，并实现了 loadData 方法以提供基于 InputStream 的字节读取逻辑。
 *
 * <p>此类通过封装的 InputStream 实例按需读取字节数据，并在处理完成后关闭该流。</p>
 *
 * @author zhitron
 */
public class CodepointSequenceLoaderByInputStream extends CodepointSequenceLoader {
    /**
     * 封装的 InputStream 实例，用于按需读取字节数据。
     * 该流在对象生命周期内保持打开状态，直到调用 close 方法显式关闭。
     */
    private final InputStream input;

    /**
     * 构造函数，初始化具有指定 InputStream、字符集和缓冲区大小的 CodepointSequenceLoaderByInputStream。
     *
     * @param input      用于读取字节数据的 InputStream，不能为 null
     * @param charset    用于解码字节数据的字符集，不能为 null
     * @param bufferSize 字符缓冲区的大小，必须大于 0
     */
    public CodepointSequenceLoaderByInputStream(InputStream input, Charset charset, int bufferSize) {
        super(charset, bufferSize);
        this.input = Objects.requireNonNull(input, "The InputStream cannot be null");
    }

    /**
     * 实现父类的 loadData 方法，从封装的 InputStream 中读取字节数据并填充到提供的 byte 数组中。
     *
     * @param buffer 要填充数据的目标 byte 数组
     * @return 实际读取的字节数，如果到达输入流末尾则返回 -1
     * @throws IOException 如果读取过程中发生 I/O 错误
     */
    @Override
    protected int loadData(byte[] buffer) throws IOException {
        return input.read(buffer);
    }

    /**
     * 实现 Closeable 接口的 close 方法，用于关闭封装的 InputStream。
     *
     * @throws IOException 如果关闭过程中发生 I/O 错误
     */
    @Override
    public void close() throws IOException {
        input.close();
    }
}
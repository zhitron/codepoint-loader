package com.github.zhitron.codepoint_loader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

/**
 * CodepointSequenceLoader 是一个抽象类，用于从字节序列加载 Unicode Code Point。
 * 它继承自 CodepointLoader，并提供基于字符集解码的通用实现。
 *
 * <p>此类通过使用 CharsetDecoder 将原始字节解码为字符，支持处理各种编码格式的数据源。
 * 子类需要实现 loadData 方法以提供具体的字节读取逻辑。</p>
 *
 * @author zhitron
 */
public abstract class CodepointSequenceLoader extends CodepointLoader {
    /**
     * 用于存储从数据源读取的原始字节数据的缓冲区。
     * 该缓冲区为 final 类型，确保其在对象生命周期内不可变。
     * 缓冲区大小通常是字符缓冲区大小的四倍，以适应多字节字符的解码需求。
     */
    private final byte[] buffer;

    /**
     * 用于处理字节数据的 NIO ByteBuffer。
     * 该缓冲区为 final 类型，确保其在对象生命周期内不可变。
     * 分配的大小是字符缓冲区大小的八倍，以确保能够容纳足够多的字节数据。
     */
    private final ByteBuffer byteBuffer;

    /**
     * 用于将字节数据解码为字符的 CharsetDecoder。
     * 该解码器为 final 类型，确保其在对象生命周期内不可变。
     * 默认配置为遇到非法输入或不可映射字符时进行替换处理。
     */
    private final CharsetDecoder charsetDecoder;

    /**
     * 标志位，指示当前字节缓冲区是否已加载完成。
     * 默认值为 true，表示初始状态下缓冲区为空且需要加载数据。
     * 当数据被加载到缓冲区后，该标志会被设置为 false，
     * 在解码完成后再次设置为 true，表示需要重新加载数据。
     */
    private boolean loaded = true;

    /**
     * 构造函数，初始化具有指定字符集和缓冲区大小的 CodepointSequenceLoader。
     *
     * @param charset    用于解码字节数据的字符集，不能为 null
     * @param bufferSize 字符缓冲区的大小，必须大于 0
     */
    public CodepointSequenceLoader(Charset charset, int bufferSize) {
        super(bufferSize);
        if (charset == null) {
            throw new NullPointerException("charset cannot be null");
        }
        this.buffer = new byte[bufferSize << 2]; // 缓冲区大小为字符缓冲区大小的4倍
        this.byteBuffer = ByteBuffer.allocateDirect(bufferSize << 3); // 分配大小为字符缓冲区大小的8倍
        this.charsetDecoder = charset.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE);
    }

    /**
     * 实现父类的 loadCharBuffer 方法，将字节数据解码为字符数据并填充到提供的 CharBuffer 中。
     *
     * <p>该方法首先检查是否需要从数据源加载新的字节数据（当 loaded 为 true 时）。
     * 如果需要，则调用 loadData 方法获取新数据，并将其放入 byteBuffer。
     * 然后使用 charsetDecoder 将字节数据解码为字符数据，并存入 charBuffer。
     * 最后根据解码结果更新 loaded 状态。</p>
     *
     * @param charBuffer 要加载数据的目标 CharBuffer
     */
    @Override
    protected final void loadCharBuffer(CharBuffer charBuffer) {
        if (loaded) {
            loaded = false;
            int len;
            try {
                len = loadData(buffer);
            } catch (IOException e) {
                throw new UncheckedIOException("Error to load byte data", e);
            }
            if (len > buffer.length) {
                throw new IllegalStateException("Error to load byte data");
            }
            if (len <= 0) {
                byteBuffer.clear();
            } else {
                byteBuffer.put(buffer, 0, len);
            }
            byteBuffer.flip();
        }
        CoderResult result = charsetDecoder.decode(byteBuffer, charBuffer, false);
        if (result.isError()) {
            try {
                result.throwException();
            } catch (CharacterCodingException e) {
                throw new UncheckedIOException("Error to decode byte data to char", e);
            }
        }
        if (result.isUnderflow()) {
            loaded = true;
            byteBuffer.compact();
        } else if (!byteBuffer.hasRemaining()) {
            byteBuffer.clear();
            loaded = true;
        }
    }

    /**
     * 抽象方法，由子类实现以提供具体的字节数据加载逻辑。
     *
     * @param buffer 要填充数据的目标 byte 数组
     * @return 实际读取的字节数，如果到达数据源末尾则返回 0 或负值
     * @throws IOException 如果读取过程中发生 I/O 错误
     */
    protected abstract int loadData(byte[] buffer) throws IOException;
}
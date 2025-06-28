package com.github.zhitron.codepoint_loader;

import com.github.zhitron.codepoint_loader.impl.*;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 工厂类，用于创建不同类型的 CodepointLoader 实例。
 *
 * @author zhitron
 */
public final class CodepointLoaderFactory {

    private CodepointLoaderFactory() {
    }

    /**
     * 从字符数组创建 CodepointLoader。
     *
     * @param input 字符数组
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(char[] input) {
        return new CodepointLoaderByCharArray(input, 1024);
    }

    /**
     * 从字符数组创建 CodepointLoader，并指定缓冲区大小。
     *
     * @param input      字符数组
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(char[] input, int bufferSize) {
        return new CodepointLoaderByCharArray(input, bufferSize);
    }

    /**
     * 从 CharBuffer 创建 CodepointLoader。
     *
     * @param input CharBuffer
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(CharBuffer input) {
        return new CodepointLoaderByCharBuffer(input, 1024);
    }

    /**
     * 从 CharBuffer 创建 CodepointLoader，并指定缓冲区大小。
     *
     * @param input      CharBuffer
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(CharBuffer input, int bufferSize) {
        return new CodepointLoaderByCharBuffer(input, bufferSize);
    }

    /**
     * 从 Reader 创建 CodepointLoader。
     *
     * @param input Reader
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(Reader input) {
        return new CodepointLoaderByReader(input, 1024);
    }

    /**
     * 从 Reader 创建 CodepointLoader，并指定缓冲区大小。
     *
     * @param input      Reader
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(Reader input, int bufferSize) {
        return new CodepointLoaderByReader(input, bufferSize);
    }

    /**
     * 从字节数组创建 CodepointSequenceLoader。
     *
     * @param input 字节数组
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(byte[] input) {
        return new CodepointSequenceLoaderByByteArray(input, StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从字节数组创建 CodepointSequenceLoader。
     *
     * @param input   字节数组
     * @param charset 字符集
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(byte[] input, Charset charset) {
        return new CodepointSequenceLoaderByByteArray(input, charset, 1024);
    }

    /**
     * 从字节数组创建 CodepointSequenceLoader，并指定缓冲区大小。
     *
     * @param input      字节数组
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(byte[] input, Charset charset, int bufferSize) {
        return new CodepointSequenceLoaderByByteArray(input, charset, bufferSize);
    }

    /**
     * 从 InputStream 创建 CodepointSequenceLoader。
     *
     * @param input 输入流
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(InputStream input) {
        return new CodepointSequenceLoaderByInputStream(input, StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从 InputStream 创建 CodepointSequenceLoader。
     *
     * @param input   输入流
     * @param charset 字符集
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(InputStream input, Charset charset) {
        return new CodepointSequenceLoaderByInputStream(input, charset, 1024);
    }

    /**
     * 从 InputStream 创建 CodepointSequenceLoader，并指定缓冲区大小。
     *
     * @param input      输入流
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(InputStream input, Charset charset, int bufferSize) {
        return new CodepointSequenceLoaderByInputStream(input, charset, bufferSize);
    }

    /**
     * 从 ReadableByteChannel 创建 CodepointSequenceLoader。
     *
     * @param input 可读通道
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(ReadableByteChannel input) {
        return new CodepointSequenceLoaderByReadableByteChannel(input, StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从 ReadableByteChannel 创建 CodepointSequenceLoader。
     *
     * @param input   可读通道
     * @param charset 字符集
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(ReadableByteChannel input, Charset charset) {
        return new CodepointSequenceLoaderByReadableByteChannel(input, charset, 1024);
    }

    /**
     * 从 ReadableByteChannel 创建 CodepointSequenceLoader，并指定缓冲区大小。
     *
     * @param input      可读通道
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(ReadableByteChannel input, Charset charset, int bufferSize) {
        return new CodepointSequenceLoaderByReadableByteChannel(input, charset, bufferSize);
    }

    /**
     * 从字符串创建 CodepointLoader。
     *
     * @param input 字符串
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(String input) {
        return new CodepointLoaderByCharArray(input.toCharArray(), 1024);
    }

    /**
     * 从字符串创建 CodepointLoader，并指定缓冲区大小。
     *
     * @param input      字符串
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(String input, int bufferSize) {
        return new CodepointLoaderByCharArray(input.toCharArray(), bufferSize);
    }

    /**
     * 从字符串创建 CodepointLoader，并指定缓冲区大小。
     *
     * @param input      字符串
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     */
    public static CodepointLoader of(String input, Charset charset, int bufferSize) {
        return new CodepointSequenceLoaderByByteArray(input.getBytes(charset), charset, bufferSize);
    }

    /**
     * 从文件路径创建 CodepointLoader（使用 UTF-8 编码）。
     *
     * @param input 文件路径
     * @return CodepointLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static CodepointLoader of(File input) throws IOException {
        return new CodepointSequenceLoaderByInputStream(new FileInputStream(input), StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从文件路径创建 CodepointLoader，并指定编码和缓冲区大小。
     *
     * @param input      文件路径
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static CodepointLoader of(File input, Charset charset, int bufferSize) throws IOException {
        return new CodepointSequenceLoaderByInputStream(new FileInputStream(input), charset, bufferSize);
    }

    /**
     * 从文件路径创建 CodepointLoader（使用 UTF-8 编码）。
     *
     * @param input 文件路径
     * @return CodepointLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static CodepointLoader of(Path input) throws IOException {
        return new CodepointSequenceLoaderByInputStream(Files.newInputStream(input), StandardCharsets.UTF_8, 1024);
    }

    /**
     * 从文件路径创建 CodepointLoader，并指定编码和缓冲区大小。
     *
     * @param input      文件路径
     * @param charset    字符集
     * @param bufferSize 缓冲区大小
     * @return CodepointLoader 实例
     * @throws IOException 如果打开文件失败
     */
    public static CodepointLoader of(Path input, Charset charset, int bufferSize) throws IOException {
        return new CodepointSequenceLoaderByInputStream(Files.newInputStream(input), charset, bufferSize);
    }
}

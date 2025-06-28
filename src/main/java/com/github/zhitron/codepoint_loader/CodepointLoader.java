package com.github.zhitron.codepoint_loader;

import java.nio.CharBuffer;
import java.util.NoSuchElementException;

/**
 * 该类定义了用于加载 Unicode Code Point 的基本操作。
 *
 * @author zhitron
 */
public abstract class CodepointLoader implements AutoCloseable {
    /**
     * 存储字符数据的缓冲区，用于读取 Unicode Code Point。
     * 该缓冲区为 final 类型，确保其在对象生命周期内不可变。
     */
    private final CharBuffer charBuffer;

    /**
     * 缓存的 Unicode Code Point，用于优化 hasNextCodepoint 和 nextCodepoint 方法。
     * 默认值 -1 表示当前没有缓存有效的 Code Point。
     */
    private int cachedCodePoint = -1;

    /**
     * 构造函数，初始化具有指定大小的字符缓冲区。
     *
     * @param bufferSize 缓冲区的大小
     */
    public CodepointLoader(int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize must be greater than 0");
        }
        this.charBuffer = CharBuffer.allocate(bufferSize);
        this.charBuffer.flip();
    }

    /**
     * 默认的关闭方法，用于释放资源或完成清理工作
     *
     * @throws Exception 如果关闭过程中发生错误，抛出此异常
     */
    @Override
    public void close() throws Exception {
    }

    /**
     * 检查是否还有下一个 Unicode Code Point 可读取。
     *
     * @return 如果有下一个 Code Point 返回 true，否则返回 false。
     */
    public final boolean hasNextCodepoint() {
        if (cachedCodePoint != -1) return true;
        int codepoint = this.peekCodepoint();
        cachedCodePoint = codepoint;
        return codepoint != -1;
    }

    /**
     * 获取下一个 Unicode Code Point，并移动读取位置。
     *
     * @return 下一个 Unicode Code Point。
     */
    public final int nextCodepoint() {
        int result = cachedCodePoint;
        if (result == -1) {
            if (!this.hasNextCodepoint()) {
                throw new NoSuchElementException("There is no next element");
            }
            result = cachedCodePoint;
        }
        cachedCodePoint = -1;
        int value = this.popCodepoint();
        if (value != result) {
            throw new RuntimeException("Got the wrong value");
        }
        return result;
    }

    /**
     * 检查当前对象是否为空
     * <p>
     * 此方法通过检查peekCodepoint()方法的返回值是否为-1来判断对象是否为空
     * 如果返回值为-1，则表示对象中没有可读取的代码点，即对象为空
     *
     * @return 如果对象为空，则返回true；否则返回false
     */
    public final boolean isEmpty() {
        return this.peekCodepoint() == -1;
    }

    /**
     * 预览当前的 Unicode Code Point，不会移动读取位置。
     *
     * @return 当前的 Unicode Code Point。
     */
    public final int peekCodepoint() {
        return this.peekCodepoint(0);
    }

    /**
     * 预览指定偏移位置的 Unicode Code Point，不会移动读取位置。
     *
     * @param offset 相对于当前位置的偏移量。
     * @return 指定偏移位置的 Unicode Code Point。
     */
    public final int peekCodepoint(int offset) {
        return this.getCodepoint(offset, false);
    }

    /**
     * 弹出代码点
     * 该方法用于从数据结构中弹出一个代码点（Unicode字符）默认情况下，不指定任何标志
     * 具体行为和返回值取决于底层数据结构和popCodepoint方法的实现
     *
     * @return int 弹出的代码点如果数据结构为空或无法弹出代码点，则返回值将反映这一点
     */
    public final int popCodepoint() {
        return this.popCodepoint(0);
    }

    /**
     * 从数据结构中弹出一个代码点（codepoint）并返回它
     * 此方法用于需要处理字符数据的高级文本处理场景
     * 它确保以正确的方式处理字符边界，避免数据损坏或不一致
     *
     * @param count 指定要弹出的代码点的数量
     * @return 返回弹出的代码点的整数值如果栈中没有足够的代码点，则行为未定义
     */
    public final int popCodepoint(int count) {
        return this.getCodepoint(count, true);
    }

    /**
     * 将当前上下文转换为字符串内容
     * 此方法通过遍历当前上下文中的所有代码点，并将它们拼接成一个字符串来实现
     * 它利用了StringBuilder来高效地构建字符串
     *
     * @return 包含当前上下文所有代码点的字符串
     */
    public final String toContent() {
        StringBuilder sb = new StringBuilder();
        while (hasNextCodepoint()) {
            int codePoint = nextCodepoint();
            sb.appendCodePoint(codePoint);
        }
        return sb.toString();
    }

    /**
     * 根据给定参数获取指定位置的 Unicode Code Point
     * 支持预览和消费两种模式，可处理代理对等复杂字符情况
     *
     * @param value   指定相对于当前位置的偏移量
     * @param consume 是否消费字符（即是否移动位置指针）
     * @return 返回对应的 Unicode Code Point，若不存在则返回 -1
     */
    protected final int getCodepoint(final int value, final boolean consume) {
        if (value < 0 || value >= charBuffer.capacity()) {
            throw new IllegalArgumentException("offset out of range at [0," + charBuffer.capacity() + ")");
        }
        tag(true, consume);
        char h, l;
        int charCount = 0, count = 0;
        try {
            while (count <= value) {
                // 判断是否有剩余字符可读
                if (!charBuffer.hasRemaining()) {
                    // 缓冲区无足够空间，重新填充数据
                    tag(false, consume);
                    charBuffer.compact();
                    loadCharBuffer(charBuffer);
                    charBuffer.flip();
                    if (!consume) {
                        charBuffer.mark();
                    }
                    charBuffer.position(charCount);
                    if (!charBuffer.hasRemaining()) {
                        return -1;
                    }
                }

                h = charBuffer.get();
                charCount++;
                // 判断是否是高代理字符
                if (Character.isHighSurrogate(h)) {
                    // 获取低代理字符
                    if (!charBuffer.hasRemaining()) {
                        tag(false, consume);
                        charBuffer.compact();
                        loadCharBuffer(charBuffer);
                        charBuffer.flip();
                        if (!consume) {
                            charBuffer.mark();
                        }
                        charBuffer.position(charCount);
                        if (!charBuffer.hasRemaining()) {
                            throw new IllegalStateException("Incomplete surrogate pair: high surrogate without low surrogate.");
                        }
                    }
                    l = charBuffer.get();
                    charCount++;
                    if (count == value) {
                        return Character.toCodePoint(h, l);
                    }
                } else {
                    if (count == value) {
                        return h;
                    }
                }
                count += 1;
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException("Error to load char data", e);
        } finally {
            // 操作完成后恢复位置并根据 consume 决定是否消费字符
            tag(false, consume);
        }
    }

    private void tag(boolean mark, boolean consume) {
        try {
            if (!consume) {
                if (mark) {
                    charBuffer.mark();
                } else {
                    charBuffer.reset();
                }
            }
        } catch (Exception ignored) {

        }
    }

    /**
     * 抽象方法，由子类实现以提供具体的字符加载逻辑。
     *
     * @param charBuffer 要加载数据的目标 CharBuffer
     */
    protected abstract void loadCharBuffer(CharBuffer charBuffer) throws Exception;
}
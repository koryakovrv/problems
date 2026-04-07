package org.example.other;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.util.Arrays;

/**
 *  RLE 
 */
public class RLECompressor {
    
    /**
     * Сжатие файла с помощью алгоритма RLE
     * @param inputFile путь к исходному файлу
     * @param outputFile путь к сжатому файлу
     * @throws IOException при ошибках ввода-вывода
     */
    public static void compressFile(String inputFile, String outputFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            compress(fis, fos);
        }
    }
    
    /**
     * Распаковка RLE файла
     * @param inputFile путь к сжатому файлу
     * @param outputFile путь к распакованному файлу
     * @throws IOException при ошибках ввода-вывода
     */
    public static void decompressFile(String inputFile, String outputFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            decompress(fis, fos);
        }
    }
    
    /**
     * Упакованный RLE с оптимизацией для неповторяющихся данных
     * Формат: 
     * - Флаг 0x00 + байт + счетчик для повторений
     * - Флаг 0xFF + длина + данные для неповторяющихся блоков
     */
    public static void compressPacked(InputStream input, OutputStream output) 
            throws IOException {
        
        PushbackInputStream pbInput = new PushbackInputStream(input, 1024);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        
        while (true) {
            int currentByte = pbInput.read();
            if (currentByte == -1) break;
            
            // Проверяем, начинается ли последовательность повторений
            int nextByte = pbInput.read();
            if (nextByte != -1 && nextByte == currentByte) {
                // Нашли повторяющуюся последовательность
                int count = 2;
                
                while (count < 255 && (nextByte = pbInput.read()) != -1 && nextByte == currentByte) {
                    count++;
                }
                
                if (nextByte != -1 && nextByte != currentByte) {
                    pbInput.unread(nextByte);
                }
                
                // Записываем маркер повторяющегося блока
                output.write(0x00);
                output.write(currentByte);
                output.write(count);
            } else {
                // Собираем блок неповторяющихся данных
                if (nextByte != -1) {
                    pbInput.unread(nextByte);
                }
                
                buffer.reset();
                buffer.write(currentByte);
                
                while ((nextByte = pbInput.read()) != -1) {
                    // Проверяем, не начинается ли новая последовательность
                    int futureByte = pbInput.read();
                    if (futureByte != -1 && futureByte == nextByte) {
                        pbInput.unread(futureByte);
                        break;
                    }
                    
                    buffer.write(nextByte);
                    
                    if (buffer.size() >= 255) break;
                    
                    if (futureByte != -1) {
                        pbInput.unread(futureByte);
                    }
                }
                
                // Записываем маркер неповторяющегося блока
                output.write(0xFF);
                output.write(buffer.size());
                buffer.writeTo(output);
            }
        }
    }
    
    /**
     * Декомпрессия упакованного RLE
     */
    public static void decompressPacked(InputStream input, OutputStream output) 
            throws IOException {
        
        while (true) {
            int flag = input.read();
            if (flag == -1) break;
            
            if (flag == 0x00) {
                // Повторяющийся блок
                int value = input.read();
                int count = input.read();
                
                byte[] bytes = new byte[count];
                Arrays.fill(bytes, (byte) value);
                output.write(bytes);
            } else if (flag == 0xFF) {
                // Неповторяющийся блок
                int length = input.read();
                byte[] data = new byte[length];
                int read = input.read(data);
                if (read != length) {
                    throw new IOException("Недостаточно данных для распаковки");
                }
                output.write(data);
            } else {
                throw new IOException("Неверный формат сжатых данных");
            }
        }
    }
    
    /**
     * Реализация RLE сжатия
     * Формат: [байт] [количество повторений]
     * Количество повторений от 1 до 255
     */
    public static void compress(InputStream input, OutputStream output) throws IOException {
        PushbackInputStream pbInput = new PushbackInputStream(input, 1);
        DataOutputStream dataOutput = new DataOutputStream(output);
        
        int currentByte;
        int nextByte = 0;
        
        while ((currentByte = pbInput.read()) != -1) {
            int count = 1;
            
            // Считаем количество повторений текущего байта
            while (count < 255 && (nextByte = pbInput.read()) != -1 && nextByte == currentByte) {
                count++;
            }
            
            // Если прочитали другой байт, возвращаем его обратно в поток
            if (nextByte != -1 && nextByte != currentByte) {
                pbInput.unread(nextByte);
            }
            
            // Записываем пару (байт, количество)
            dataOutput.writeByte(currentByte);
            dataOutput.writeByte(count);
        }
        
        dataOutput.flush();
    }
    
    /**
     * Реализация RLE распаковки
     */
    public static void decompress(InputStream input, OutputStream output) throws IOException {
        DataInputStream dataInput = new DataInputStream(input);
        
        try {
            while (true) {
                // Читаем байт и количество повторений
                int value = dataInput.readUnsignedByte();
                int count = dataInput.readUnsignedByte();
                
                // Записываем байт count раз
                for (int i = 0; i < count; i++) {
                    output.write(value);
                }
            }
        } catch (EOFException e) {
            // Достигнут конец файла - нормальное завершение
        }
        
        output.flush();
    }
    
}
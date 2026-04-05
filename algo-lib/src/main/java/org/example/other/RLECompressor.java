package org.example.other;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;

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
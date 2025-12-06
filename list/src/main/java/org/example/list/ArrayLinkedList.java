package org.example.list;

import java.util.NoSuchElementException;

/**
 * Связный список на смежной памяти
 */
public class ArrayLinkedList<E> {
    public static final int DEFAULT_CAPACITY = 50;
    public static final float DEFAULT_FACTOR = 1.3f;
    
    // параллельные массивы для организации списка
    private E[] elements;    // элементы списка
    private int[] next;      // указатели на следующий элемент (индекс или -1)
    private int[] free;      // стек свободных элементов
    private int freeTop;     // вершина стека свободных элементов
    
    private int head;        // индекс первого элемента
    private int tail;        // индекс последнего элемента
    private int size;
    private float loadFactor;
    
    public ArrayLinkedList() {
        this(DEFAULT_CAPACITY, DEFAULT_FACTOR);
    }
    
    public ArrayLinkedList(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        if (loadFactor <= 1.0f) {
            throw new IllegalArgumentException("Load factor must be > 1.0");
        }
        
        elements = (E[]) new Object[initialCapacity];
        next = new int[initialCapacity];
        free = new int[initialCapacity];
        
        // Инициализируем стек свободных элементов
        for (int i = 0; i < initialCapacity; i++) {
            free[i] = i;
            next[i] = -1;
        }
        
        freeTop = initialCapacity - 1;  // стек растет сверху вниз
        head = tail = -1;  // список пустой
        size = 0;
        this.loadFactor = loadFactor;
    }
    
    public void add(final E element) {
        if (freeTop < 0) {
            grow();
        }
        
        int index = free[freeTop--];  // берем свободный индекс
        
        // Заполняем элемент
        elements[index] = element;
        next[index] = -1;  // новый элемент пока ни на что не указывает
        
        // Добавляем элемент в конец списка
        if (head == -1) {
            // Первый элемент
            head = tail = index;
        } else {
            // Присоединяем к хвосту
            next[tail] = index;
            tail = index;
        }
        
        size++;
    }
    
    public boolean remove(int index) {
        if (index < 0 || index >= size || head == -1) {
            return false;
        }
        int toRemove = 0;
        
        if (index == 0) {
            // Удаляем первый элемент
            toRemove = head;
            head = next[head];
            if (head == -1) {
                tail = -1;  // список стал пустым
            }
        } else {
            // Находим элемент перед удаляемым
            int current = head;
            for (int i = 0; i < index - 1; i++) {
                current = next[current];
            }
            
            toRemove = next[current];
            next[current] = next[toRemove];
            
            if (toRemove == tail) {
                tail = current;  // обновляем хвост если нужно
            }
        }
        
        freeElement(toRemove);
        size--;
        return true;
    }
    
    private void freeElement(int index) {
        elements[index] = null;  // для сборки мусора
        next[index] = -1;
        free[++freeTop] = index;  // возвращаем в стек свободных
    }
    
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        int current = head;
        for (int i = 0; i < index; i++) {
            current = next[current];
        }
        
        return elements[current];
    }
    
    public E getFirst() {
        if (head == -1) {
            throw new NoSuchElementException("List is empty");
        }
        return elements[head];
    }
    
    public E getLast() {
        if (tail == -1) {
            throw new NoSuchElementException("List is empty");
        }
        return elements[tail];
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    private void grow() {
        int newLength = Math.max(2, (int)(elements.length * loadFactor));
        
        // Сохраняем старые массивы
        E[] oldElements = elements;
        int[] oldNext = next;
        int[] oldFree = free;
        
        // Создаем новые массивы
        elements = (E[]) new Object[newLength];
        next = new int[newLength];
        free = new int[newLength];
        
        // Копируем старые данные
        System.arraycopy(oldElements, 0, elements, 0, oldElements.length);
        System.arraycopy(oldNext, 0, next, 0, oldNext.length);
        
        // Инициализируем новые свободные элементы
        for (int i = oldElements.length; i < newLength; i++) {
            free[i - oldElements.length] = i;
            next[i] = -1;
        }
        
        // Копируем старые свободные элементы
        System.arraycopy(oldFree, 0, free, newLength - oldElements.length, oldFree.length);
        freeTop += newLength - oldElements.length;
    }
    
    @Override
    public String toString() {
        if (head == -1) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        int current = head;
        while (current != -1) {
            sb.append(elements[current]);
            if (next[current] != -1) {
                sb.append(", ");
            }
            current = next[current];
        }
        sb.append("]");
        return sb.toString();
    }
}
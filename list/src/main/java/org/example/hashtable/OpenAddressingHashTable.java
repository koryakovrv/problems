package org.example.hashtable;
import java.util.Arrays;

/*
 * Хэш-таблица с квадратичным пробированием
 */
public class OpenAddressingHashTable {
    private static final int DEFAULT_CAPACITY = 17;
    private static final double LOAD_FACTOR_THRESHOLD = 0.7;
    private static final Entry DELETED = new Entry(null, null); // шапка
    
    private Entry[] table;
    private int size;
    private int capacity;
    
    // Вспомогательный класс для хранения записей
    private static class Entry {
        Object key;
        Object value;
        
        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
    
    public OpenAddressingHashTable() {
        this(DEFAULT_CAPACITY);
    }
    
    public OpenAddressingHashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        this.table = new Entry[capacity];
        this.size = 0;
    }
    
    /**
     * Основная хэш-функция
     */
    private int hash(Object key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
    /**
     * Вспомогательная хэш-функция для квадратичного пробирования
     */
    private int quadraticProbe(int hash, int i) {
        return (hash + i * i) % capacity;
    }
    
    /**
     * Вставка ключа-значения в таблицу
     */
    public void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("Ключ не может быть null");
        }
        
        // Проверка необходимости рехэширования
        if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
            rehash();
        }
        
        int hash = hash(key);
        int i = 0;
        int index = quadraticProbe(hash, i);
        
        while (i < capacity) {
            Entry entry = table[index];
            
            // Если ячейка пуста или помечена как удаленная
            if (entry == null || entry == DELETED) {
                table[index] = new Entry(key, value);
                size ++;
                return;
            }
            
            // Если ключ уже существует - обновляем значение
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
            
            i++;
            index = quadraticProbe(hash, i);
        }
        
        // Если не нашли место - таблица полная (теоретически не должно случиться после рехэширования)
        throw new IllegalStateException("Хэш-таблица переполнена");
    }
    
    /**
     * Поиск значения по ключу с реализацией ленивого удаления
     */
    public Object get(Object key) {
        if (key == null) {
            return null;
        }
        
        int hash = hash(key);
        int i = 0;
        int index = quadraticProbe(hash, i);
        int firstDeletedIndex = -1; // для ленивого удаления
        
        while (i < capacity && table[index] != null) {
            Entry entry = table[index];
            
            if (entry == DELETED) {
                if (firstDeletedIndex == -1) {
                    firstDeletedIndex = index; // индекс ближайшего удаленного элемента
                }
            } else if (entry.key.equals(key)) {
                if (firstDeletedIndex > -1 && firstDeletedIndex < index) {
                    table[firstDeletedIndex] = table[index];
                    table[index] = DELETED;
                }
                return entry.value;
            }
            
            i ++;
            index = quadraticProbe(hash, i);
        }
        
        return null; // Ключ не найден
    }
    
    /**
     * Проверка наличия ключа в таблице
     */
    public boolean containsKey(Object key) {
        return get(key) != null;
    }
    
    /**
     * Удаление ключа из таблицы
     */
    public boolean remove(Object key) {
        if (key == null) {
            return false;
        }
        
        int hash = hash(key);
        int i = 0;
        int index = quadraticProbe(hash, i);
        
        while (i < capacity && table[index] != null) {
            Entry entry = table[index];
            
            if (entry != DELETED && entry.key.equals(key)) {
                table[index] = DELETED; // Помечаем как удаленное
                size--;
                return true;
            }
            
            i++;
            index = quadraticProbe(hash, i);
        }
        
        return false; // Ключ не найден
    }
    
    /**
     * Увеличение размера таблицы и рехэширование всех элементов
     */
    private void rehash() {
        int newCapacity = capacity * 2;
        Entry[] oldTable = table;
        table = new Entry[newCapacity];
        capacity = newCapacity;
        size = 0;
        
        for (Entry entry : oldTable) {
            if (entry != null && entry != DELETED) {
                put(entry.key, entry.value);
            }
        }
    }
    
    /**
     * Получение количества элементов в таблице
     */
    public int size() {
        return size;
    }
    
    /**
     * Проверка, пуста ли таблица
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Очистка таблицы
     */
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }
    
    /**
     * Вывод содержимого таблицы (для отладки)
     */
    public void printTable() {
        System.out.println("Хэш-таблица (размер: " + size + ", вместимость: " + capacity + "):");
        for (int i = 0; i < capacity; i++) {
            Entry entry = table[i];
            if (entry == null) {
                System.out.println(i + ": null");
            } else if (entry == DELETED) {
                System.out.println(i + ": DELETED");
            } else {
                System.out.println(i + ": [" + entry.key + " -> " + entry.value + "]");
            }
        }
    }
    
    // Пример использования
    public static void main(String[] args) {
        OpenAddressingHashTable hashTable = new OpenAddressingHashTable(9);
        
        // Вставка элементов
        hashTable.put(1, 10);
        hashTable.put(11, 20);
        hashTable.put(14, 30);
        hashTable.put(13, 40);
        hashTable.put(23, 50);
        
        System.out.println("После вставки 5 элементов:");
        hashTable.printTable();
        
        // Поиск элементов
        System.out.println("\nПоиск элементов:");
        System.out.println("23 -> " + hashTable.get(23));
        System.out.println("11 -> " + hashTable.get(11));
        System.out.println("5 -> " + hashTable.get(5));
        
        // Обновление элемента
        hashTable.put(11, 100);
        System.out.println("\nПосле обновления 11:");
        System.out.println("11 -> " + hashTable.get(11));
        
        // Проверка наличия ключа
        System.out.println("\nПроверка наличия ключей:");
        System.out.println("Содержит '23'? " + hashTable.containsKey(23));
        System.out.println("Содержит 'pear'? " + hashTable.containsKey("pear"));
        
        // Удаление элемента
        System.out.println("\nУдаление 14: " + hashTable.remove(14));
        System.out.println("После удаления 14:");
        hashTable.printTable();
        
        // Попытка найти удаленный элемент
        System.out.println("\nПоиск удаленного 14: " + hashTable.get("banana"));
        
        // Добавление новых элементов для демонстрации рехэширования
        hashTable.put("melon", 60);
        hashTable.put("peach", 70);
        hashTable.put("pear", 80);
        
        System.out.println("\nПосле добавления еще 3 элементов (должно произойти рехэширование):");
        hashTable.printTable();
    }
}
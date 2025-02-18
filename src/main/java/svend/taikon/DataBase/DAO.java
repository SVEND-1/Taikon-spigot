package svend.taikon.DataBase;

import org.bson.Document;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {
    // Создание записи
    boolean insert(T model);

    // Чтение записи по ID
    T read(ID id);

    // Обновление записи
    boolean update(T model);

    // Удаление записи
    boolean delete(T model);

    // Получение всех записей
    List<T> getAll();
    T mapDocumentToUser(Document doc);
}
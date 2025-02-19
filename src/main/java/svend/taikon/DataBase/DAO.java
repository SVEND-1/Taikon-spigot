package svend.taikon.DataBase;

import org.bson.Document;

import java.util.List;
import java.util.Optional;

public interface DAO<T, ID> {
    boolean insert(T model);

    T read(ID id);

    boolean update(T model);

    boolean delete(T model);

    List<T> getAll();
}
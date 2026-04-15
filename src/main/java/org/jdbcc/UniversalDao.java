package org.jdbcc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

// 1)
// реализовать БД для сотрудников — отделов
// создать классы таблиц
// создать по два бина репозиториев (по одному на таблицу)
// проверить работоспособность
// 2)
// изменить findAll, так, чтобы он возвращал Stream
// реализовать findAll так, чтобы можно было управлять объёмом данных, запрошенных за один раз из базы


public class UniversalDao<T> {
    protected final Connection connect;
    protected final Class<T> clz;

    public UniversalDao(Connection connect, Class<T> clz) {
        this.connect = connect;
        this.clz = clz;
    }

    public void synchronize() {
        // синхронизировать записи?
    }

    public List<T> findAll() {
        // найти записи и породить объекты
        String sql = "SELECT * FROM "+clz.getSimpleName();
        List<T> answer = new ArrayList<>();
        try{
            PreparedStatement statement = connect.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                answer.add(convertToObject(rs));
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }
    

    public boolean delete(int id) {
        // удалить запись(и видимо объект)
        String sql = "DELETE FROM "+clz.getSimpleName()+" WHERE id="+id;//не факт что пк колонка называется id
        try{
            PreparedStatement statement = connect.prepareStatement(sql);
            return statement.executeUpdate()>0;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(T t){
        // удалить запись(и видимо объект)
        return delete(findIdFromT(t));
    }

    public int insert(T t){
        // выполнить вставку
        // какой sql нужен : INSERT INTO Employee(name, salary, departmentId) VALUES (?, ?, ?)

        String sql = "INSERT INTO "+clz.getSimpleName()+"(";
        String sql2 = " VALUES(";
        Field[] fields = clz.getDeclaredFields();
        Object[] values= new Object[fields.length];
        int iterator = 0;
        if (fields.length==0) throw new RuntimeException("у класса "+clz.getSimpleName()+" нет полей");
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id") || field.getAnnotation(PK.class)!=null) continue;
            sql+=field.getName()+",";
            sql2+="?,";
            try {
                values[iterator] = field.get(t);
                iterator++;
            } catch (IllegalAccessException e) {
                throw new GetValueException("не удалось получить значение через поле " + field.getName(), e);
            }
        }
        if (iterator == 0) {
            throw new RuntimeException("у класса " + clz.getSimpleName() + " нет полей для вставки");
        }
        char[] sqlbeta= sql.toCharArray();
        char[] sqlbeta2= sql2.toCharArray();
        String sqlfin = new String(sqlbeta,0,sqlbeta.length-1)+")"+new String(sqlbeta2,0,sqlbeta2.length-1)+")";
        try {
            PreparedStatement statement = connect.prepareStatement(sqlfin);
            for (int i = 0; i < iterator; i++) {
                statement.setObject(i+1, values[i]);
            }
            return statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void update(T t) {
        // обновить запись в бд новыми данными по объекту
        // какой sql нужен : UPDATE TableName SET field1 = ?, field2 = ?, field3 = ? WHERE id = ?
        String sql = "UPDATE "+clz.getSimpleName()+" SET ";
        Field[] fields = clz.getDeclaredFields();
        Object[] values= new Object[fields.length];
        int iterator = 0;
        if (fields.length==0) throw new RuntimeException("у класса "+clz.getSimpleName()+" нет полей");
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("id") || field.getAnnotation(PK.class)!=null) continue;
            sql+=field.getName()+"=?,";
            try {
                values[iterator] = field.get(t);
                iterator++;
            } catch (IllegalAccessException e) {
                throw new GetValueException("не удалось получить значение через поле " + field.getName(), e);
            }
        }
        if (iterator == 0) {
            throw new RuntimeException("у класса " + clz.getSimpleName() + " нет полей для обновления");
        }
        char[] sqlbeta= sql.toCharArray();
        String sqlfin = new String(sqlbeta,0,sqlbeta.length-1)+" WHERE id=?";
        try {
            PreparedStatement statement = connect.prepareStatement(sqlfin);
            for (int i = 0; i < iterator; i++) {
                statement.setObject(i+1, values[i]);
            }
            statement.setObject(iterator + 1, findIdFromT(t));
            statement.executeUpdate();
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String findPKName() {// не факт что нужен не факт что будет рабтать как задумано
        throw new UnsupportedOperationException("не реализовано");
    }
    
    protected int findIdFromT(T t) {
        // есть т - это класс у него поля как у таблицы столбцы, там есть пк=id
        // достать из него пк=айди по аннотации пк
        
            // проверить поля на аннотацию
            // проверить методы на аннотацию, приоритетно брать по методу
            // нет аннотации вообще - имя поля должно быть id
        
        // инвокаем метод по аннотации
        Method[] mthds = clz.getDeclaredMethods();
        for (Method method : mthds) {
            method.setAccessible(true);
            if(method.getAnnotation(PK.class)!=null){
                // проверяем что возвращает инт или Интегер
                if(!(method.getReturnType() == int.class || method.getReturnType() == Integer.class)) continue;
                if (method.getParameterCount() != 0) continue;//?
                try {
                    Object value = method.invoke(t);
                    if (value == null) throw new GetPKException("PK через метод " + method.getName() + " равен null");
                    return (Integer) value;
                } catch (InvocationTargetException e) {
                    throw new GetPKException("метод " + method.getName() + " выбросил исключение", e.getCause());
                }
                catch (IllegalAccessException e) {
                    throw new GetPKException("не удалось получить PK через метод " + method.getName(), e);
                }
            }
        }

        // поле на аннотацию:
        Field[] fields = clz.getDeclaredFields();
        Field rawId = null;
        for (Field field : fields) {
            field.setAccessible(true);
            if (rawId==null && field.getName().equals("id") &&
                (field.getType() == int.class || field.getType() == Integer.class)
                ) {
                rawId=field;// сохр поле с именем id
            }
            if (field.getAnnotation(PK.class)!=null) {// берем первую попавшуюся с аннотацией
                if (!(field.getType() == int.class || field.getType() == Integer.class)) continue;
                try {
                    Object value = field.get(t);
                    if (value == null) throw new GetPKException("PK через метод " + method.getName() + " равен null");
                    return (Integer) value;
                } catch (IllegalAccessException e) {
                    throw new GetPKException("не удалось получить PK через поле " + field.getName(), e);
                }
            }

        }
        if (rawId==null) throw new NotFoundPKException("в классе " + clz.getSimpleName() + " не удалось найти ни @PK, ни поле id");
        try {
                Object value = rawId.get(t);
            if (value == null) throw new GetPKException("PK через метод " + method.getName() + " равен null");
            return (Integer) value;
        } catch (IllegalAccessException e) {
            throw new GetPKException("не удалось получить PK через поле " + rawId.getName(), e);
        }
    }

    protected T convertToObject(ResultSet rs) throws Exception {
        // конвертировать строчку в наш объект таблицы(класса таблицы)

        T obj = clz.getConstructor().newInstance();//создаем экзмепляр
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            field.set(obj, rs.getObject(fieldName));
        }
        return obj;
    }
}
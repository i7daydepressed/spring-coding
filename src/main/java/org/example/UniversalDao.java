package org.example;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jdbcc.PK;

// 1)
// реализовать БД для сотрудников — отделов
// создать классы таблиц
// создать по два бина репозиториев (по одному на таблицу)
// проверить работоспособность
// 2)
// изменить findAll, так, чтобы он возвращал Stream
// реализовать findAll так, чтобы можно было управлять объёмом данных, запрошенных за один раз из базы
// uT4sBNoI5A5FchmGKXd0

public class UniversalDao<T> {
    private Connection connect;
    private Class<T> clz;
    private List<T> findAll;

    public void Synxronize(){

    }

    public UniversalDao(Class<T> clz,Connection connect){
        this.connect = connect;
        this.clz = clz;
    }

    public List<T> findAll(){
        String sql = "SELECT * FROM "+clz.getSimpleName();
        List<T> answer = new ArrayList();
        try{
            PreparedStatement statement = connect.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                answer.add(convertToObject(rs));
            }
        }catch (Exception e) {
            throw new RuntimeException();
        }
            return answer;
    }
    public int delete(int id){
        String sql = "DELETE "+id+" FROM "+clz.getSimpleName();
        try {
            Statement statement = connect.createStatement();
            // ResultSet rs = statement.executeQuery();
            // statement.executeUpdate(sql);
            return statement.executeUpdate(sql)>0;
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public int delete(T t){
        return delete(findIdFromT(t));
    }
    public int insert(T t){
        String sql = "INSERT * FROM ?";


        return;
    }
    public void update(T t){

    }
    private int findIdFromT(T t){
        // Method[] mthd = clz.getMethods();
        // for (Method method : mthd) {
        // //     Annotation annotation = method.getDeclaredAnnotationsByType(GetRowId.class);
        // //     if(! (annotation == null)){
        // //         throw new IdFromTableNotFound();
        // //     }
        //     // if(method.getName().equals("getId")){
        //     // }
        // }// - хотел через метод получить ПК но получаем по полю прост
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (! (field.getAnnotation(PK.class)==null)) {
                return field.
            }
        }
        return ;
    }
    private T convertToObject(ResultSet rs) throws Exception{
        T obj = clz.getConstructor().newInstance();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            field.set(obj, rs.getObject(fieldName));
        }
        return obj;
    }
}

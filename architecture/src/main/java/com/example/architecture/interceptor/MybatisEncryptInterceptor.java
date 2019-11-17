package com.example.architecture.interceptor;


import com.example.architecture.annotation.EncryptField;
import com.example.architecture.tool.AESEncryptUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * 1、此方法用于拦截MappedStatement对象，对敏感数据做加密处理后重新拼接sql入库。
 * 2、敏感信息如身份证号码、手机号、姓名、银行卡号等，需要在对应的pojo字段上加@Encrypt标注。
 * 3、这里采用的AES对称加密方式。
 */
//注意这里导入的包选择appache.ibatis的包

@Intercepts({
        @Signature(type = Executor.class,
                   method = "update",
                   args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,
                   method = "query",
                   args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
           })
public class MybatisEncryptInterceptor implements Interceptor {

    private String key = "12345678901234567";
    private String prefix = "@cipher@";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        //注解中method的值
        String methodName = invocation.getMethod().getName();
        //sql类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if ("update".equals(methodName)) {
            Object object = invocation.getArgs()[1];
//            Date currentDate = new Date(System.currentTimeMillis());
            //对有要求的字段填值
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                Field[] fields = object.getClass().getDeclaredFields();
                for (Field f : fields) {
//                    if(f.isAnnotationPresent(EncryptField.class) && f.getGenericType().toString().equals("String")){
                    if (f.isAnnotationPresent(EncryptField.class) && f.getGenericType().equals(String.class)) {
                        f.setAccessible(true);
                        String oldValue = (String) f.get(object);
                        //encrypt
//                        byte[] encrypt = AESUtil.encrypt(oldValue, key);
//                        String base64 = Base64.getEncoder().encodeToString(encrypt);
                        String base64 = AESEncryptUtil.aesEncrypt(oldValue, key);
                        String newValue = prefix + base64;
                        f.set(object, newValue);
                    }
                }
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
//                Field fieldModifyTime = object.getClass().getDeclaredField("modifyTime");
//                fieldModifyTime.setAccessible(true);
//                fieldModifyTime.set(object, currentDate);
//                log.info("更新操作时设置modify_time:{}", currentDate);
            }
        } else if ("query".equals(methodName)) {
            Object object = invocation.getArgs()[1];
            //对有要求的字段填值
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field f : fields) {
//                    if(f.isAnnotationPresent(EncryptField.class) && f.getGenericType().toString().equals("String")){
                if (f.isAnnotationPresent(EncryptField.class) && f.getGenericType().equals(String.class)) {
                    f.setAccessible(true);
                    String oldValue = (String) f.get(object);
                    String base64 = AESEncryptUtil.aesEncrypt(oldValue, key);
                    String newValue = prefix + base64;
                    f.set(object, newValue);
                }
            }
        }
        return invocation.proceed();

    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
//        this.properties = properties;
    }
}

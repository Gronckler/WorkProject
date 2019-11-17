package com.example.architecture.interceptor;

import com.example.architecture.tool.AESEncryptUtil;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.*;

/**
 * 1、此方法用于拦截MappedStatement对象，对敏感数据做加密处理后重新拼接sql入库。
 * 2、敏感信息如身份证号码、手机号、姓名、银行卡号等，需要在对应的pojo字段上加@Encrypt标注。
 * 3、这里采用的AES对称加密方式。
 */
//注意这里导入的包选择appache.ibatis的包
@Intercepts({
//        @Signature(type = StatementHandler.class, method = "prepare", args = {Collection.class, Object.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class MybatisDecryptInterceptor implements Interceptor {

    private String key = "12345678901234567";
    private String prefix = "@cipher@";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object proceed = invocation.proceed();
        if (proceed instanceof ArrayList<?>) {
            System.out.println("list");
            List<?> list = (ArrayList<?>) proceed;
            for (Object value : list) {
                if(value instanceof Map){//用Map接收sql返回值的情况
                    DecryptMapField(value);
                }else{//用pojo接收sql返回值的情况
                    System.out.println("pojo");
                    DecryptPojoField(value);
                }
            }
        } else {
            System.out.println("pojo");
            DecryptPojoField(proceed);
        }
        return proceed;
    }

    private void DecryptMapField(Object value) throws Exception {
        Map map = (Map)value;
        Set<String> set = map.keySet();
        for(String k : set){
            Object o = map.get(k);
            if(o instanceof String &&((String)o).startsWith(prefix)){
                String tmpValue = o.toString();
                if(!StringUtils.isEmpty(tmpValue)){
                    String replace = tmpValue.replace(prefix, "");
                    String v = AESEncryptUtil.aesDecrypt(replace, key);
                    map.put(k,v);//对加密过的value解密后重新放回map
                }
            }
        }
    }

    private void DecryptPojoField(Object value) throws Exception {

        System.out.println("value="+value.toString());
        Field[] fields = value.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = field.get(value);
            System.out.println("field="+field+",fieldValue="+fieldValue);
            if (fieldValue == null) {
                continue;
            }
            String oldValue = fieldValue.toString();
            if (oldValue.startsWith(prefix)) {
                String newValue = oldValue.replace(prefix, "");
                String s = AESEncryptUtil.aesDecrypt(newValue, key);
                System.out.println("解密后s="+s);
                field.set(value,s);
            }
        }
    }


    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }


}

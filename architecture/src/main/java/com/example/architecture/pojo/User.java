package com.example.architecture.pojo;

import com.example.architecture.annotation.EncryptField;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@RequiredArgsConstructor
//@NoArgsConstructor
public class User {

    private int id;

    private String name;

    @EncryptField
    private String password;

    private String remark;


}

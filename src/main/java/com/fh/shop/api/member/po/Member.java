package com.fh.shop.api.member.po;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable {
    private Long id;

    private String userName;

    private String password;

    private String realName;

    private String phone;
    @TableField(exist = false)
    private String authCode;

    private Long area1;

    private Long area2;

    private Long area3;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private String email;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public Long getArea1() {
        return area1;
    }

    public void setArea1(Long area1) {
        this.area1 = area1;
    }

    public Long getArea2() {
        return area2;
    }

    public void setArea2(Long area2) {
        this.area2 = area2;
    }

    public Long getArea3() {
        return area3;
    }

    public void setArea3(Long area3) {
        this.area3 = area3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

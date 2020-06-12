package com.zsw.entitys;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by zhangshaowei on 2020/6/12.
 */
@Entity
@Table(name = "file", schema = "dictionary")
public class FileEntity extends IDEntity{
    private Integer id;
    private String name;
    private String fileUrl;
    private String fileCode;
    private Timestamp createTime;
    private Integer createUser;
    private Timestamp updateTime;
    private Integer updateUser;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", length=11, nullable=false, unique=true, insertable=true, updatable=false)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }



    @Basic
    //@Column(name = "name", nullable = false, length = 80)
    @NotNull
    @Length(max = 80)
    @Column(name = "name", unique = false,  nullable = false, insertable = true, updatable = true, length = 80)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }





    @Basic
    @NotNull
    @Length(max = 100)
    @Column(name = "file_url", unique = false,  nullable = false, insertable = true, updatable = true, length = 100)
    public String getFileUrl() {
        return name;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }



    @Basic
    @NotNull
    @Length(max = 100)
    @Column(name = "file_code", unique = false,  nullable = false, insertable = true, updatable = true, length = 100)
    public String getFileCode() {
        return name;
    }
    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    @Basic
    //@Column(name = "create_time", nullable = false)
    @Column(name = "create_time", nullable = true, unique = false, insertable = true, updatable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    //@Column(name = "create_user", nullable = false)
//    @NotNull
//    @Min(1L)
    @Column(name = "create_user", length = 11, nullable = true, unique = false, insertable = true, updatable = false)
    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    @Basic
    //@Column(name = "update_time", nullable = false)
    @Column(name = "update_time", nullable = true, unique = false, insertable = true, updatable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    //@Column(name = "update_user", nullable = false)
    @Column(name = "update_user", length = 11, nullable = true, unique = false, insertable = true, updatable = true)
    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntity that = (FileEntity) o;

        if (id != that.id) return false;
        if (createUser != that.createUser) return false;
        if (updateUser != that.updateUser) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (fileUrl != null ? !fileUrl.equals(that.fileUrl) : that.fileUrl != null) return false;
        if (fileCode != null ? !fileCode.equals(that.fileCode) : that.fileCode != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (fileUrl != null ? fileUrl.hashCode() : 0);
        result = 31 * result + (fileCode != null ? fileCode.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + createUser;
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + updateUser;
        return result;
    }
}

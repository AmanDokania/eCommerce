package com.example.ecommerce.Model;

public class Products {
public String pname,category,description,pid,price,image,time,date,productstatus;

    public Products() {

    }

    public Products(String pname, String category, String description, String pid, String price, String image, String time, String date, String state, String productstatus) {
        this.pname = pname;
        this.category = category;
        this.description = description;
        this.pid = pid;
        this.price = price;
        this.image = image;
        this.time = time;
        this.date = date;
        this.productstatus = productstatus;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(String productstatus) {
        this.productstatus = productstatus;
    }
}

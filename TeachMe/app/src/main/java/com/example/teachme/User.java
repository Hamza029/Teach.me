package com.example.teachme;

public class User {

    private String email, key, name, institution, imageURL, phone, adress, academic, subject;
    private int gender;
    private boolean tutor, student;
    private String isStudent, isTeacher, userGender;

    public User(String name, String institution, String imageURL, String adress, String key) {
        this.name = name;
        this.institution = institution;
        this.imageURL = imageURL;
        this.adress = adress;
        this.key = key;
    }

    public User(String email, String key, String name, String institution, String imageURL, String phone, String adress, String academic, String subject, int gender, boolean tutor, boolean student) {

        this.email = email;
        this.key = key;
        this.name = name;
        this.institution = institution;
        this.imageURL = imageURL;
        this.phone = phone;
        this.adress = adress;
        this.academic = academic;
        this.subject = subject;
        this.gender = gender;
        this.tutor = tutor;
        this.student = student;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getAcademic() {
        return academic;
    }

    public void setAcademic(String academic) {
        this.academic = academic;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setTutor(boolean tutor) {
        this.tutor = tutor;
    }

    public boolean isStudent() {
        return student;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }

    public String getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(String isStudent) {
        this.isStudent = isStudent;
    }

    public String getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(String isTeacher) {
        this.isTeacher = isTeacher;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}

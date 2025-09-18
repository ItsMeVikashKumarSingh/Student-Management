package com.example.StudentManagement.entity;

public class Document {

    private Integer studentRoll;
    private String aadharName;
    private String panName;
    private String marksheetName;
    private Boolean aadharUploaded;
    private Boolean panUploaded;
    private Boolean marksheetUploaded;

    public Document() {}

    public Document(Integer studentRoll, String aadharName, String panName, String marksheetName,
                    Boolean aadharUploaded, Boolean panUploaded, Boolean marksheetUploaded) {
        this.studentRoll = studentRoll;
        this.aadharName = aadharName;
        this.panName = panName;
        this.marksheetName = marksheetName;
        this.aadharUploaded = aadharUploaded;
        this.panUploaded = panUploaded;
        this.marksheetUploaded = marksheetUploaded;
    }

    public Integer getStudentRoll() { return studentRoll; }
    public void setStudentRoll(Integer studentRoll) { this.studentRoll = studentRoll; }
    public String getAadharName() { return aadharName; }
    public void setAadharName(String aadharName) { this.aadharName = aadharName; }
    public String getPanName() { return panName; }
    public void setPanName(String panName) { this.panName = panName; }
    public String getMarksheetName() { return marksheetName; }
    public void setMarksheetName(String marksheetName) { this.marksheetName = marksheetName; }
    public Boolean getAadharUploaded() { return aadharUploaded; }
    public void setAadharUploaded(Boolean aadharUploaded) { this.aadharUploaded = aadharUploaded; }
    public Boolean getPanUploaded() { return panUploaded; }
    public void setPanUploaded(Boolean panUploaded) { this.panUploaded = panUploaded; }
    public Boolean getMarksheetUploaded() { return marksheetUploaded; }
    public void setMarksheetUploaded(Boolean marksheetUploaded) { this.marksheetUploaded = marksheetUploaded; }
}

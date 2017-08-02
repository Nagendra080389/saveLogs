package hudson.plugins.saveLogs;


import java.io.Serializable;

public class PMDStructure implements Serializable{
    private String id;
    private String fileName;
    private String salesforceID;
    private String classname;
    private Integer lineNumber;
    private String reviewFeedback;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSalesforceID() {
        return salesforceID;
    }

    public void setSalesforceID(String salesforceID) {
        this.salesforceID = salesforceID;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getReviewFeedback() {
        return reviewFeedback;
    }

    public void setReviewFeedback(String reviewFeedback) {
        this.reviewFeedback = reviewFeedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PMDStructure that = (PMDStructure) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

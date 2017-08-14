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
    private String branchName;

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

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PMDStructure that = (PMDStructure) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (salesforceID != null ? !salesforceID.equals(that.salesforceID) : that.salesforceID != null) return false;
        if (classname != null ? !classname.equals(that.classname) : that.classname != null) return false;
        if (lineNumber != null ? !lineNumber.equals(that.lineNumber) : that.lineNumber != null) return false;
        if (reviewFeedback != null ? !reviewFeedback.equals(that.reviewFeedback) : that.reviewFeedback != null)
            return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (salesforceID != null ? salesforceID.hashCode() : 0);
        result = 31 * result + (classname != null ? classname.hashCode() : 0);
        result = 31 * result + (lineNumber != null ? lineNumber.hashCode() : 0);
        result = 31 * result + (reviewFeedback != null ? reviewFeedback.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}

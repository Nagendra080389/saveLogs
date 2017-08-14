package hudson.plugins.saveLogs;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.console.AnnotatedLargeText;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.*;
import hudson.util.FormValidation;
import org.bson.Document;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class SaveLogsPublisher extends Notifier {

    private final String hostName;
    private final String port;
    private final boolean saveToMongoDB;
    private final String logFilePath;

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public SaveLogsPublisher(String hostName, String port, boolean saveToMongoDB, String logFilePath) {
        this.hostName = hostName;
        this.port = port;
        this.saveToMongoDB = saveToMongoDB;
        this.logFilePath = logFilePath;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.BUILD;
    }

    /**
     * We'll use this from the <tt>config.jelly</tt>.
     */
    public String getHostName() {
        return hostName;
    }

    public String getPort() {
        return port;
    }

    public boolean getSaveToMongoDB() {
        return saveToMongoDB;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    /**
     * Save to Mongo DB
     */
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {

        MongoClient mongoClient = null;
        try {
            if (saveToMongoDB) {
                try {

                    mongoClient = MongoConnection.getMongoConnection("localhost", "27017");
                    MongoDatabase pmdReview = mongoClient.getDatabase("pmdReview");
                    MongoCollection<Document> salesForceClass = pmdReview.getCollection("SalesForceClass");
                    // Read the logs
                    final File folder = new File(logFilePath);
                    for (final File fileEntry : folder.listFiles()) {
                        if (fileEntry.isFile()) {
                            Path filePath = Paths.get(fileEntry.toURI());
                            if (Files.isRegularFile(filePath)) {
                                String[] split1 = filePath.toString().split("\\\\");
                                String fileName = split1[3].substring(0, split1[3].length() - 4);
                                BasicDBObject fields = new BasicDBObject("fileName", fileName);
                                Document recordPresent = salesForceClass.find(fields).first();
                                if (recordPresent == null) {
                                    try {
                                        //String FILE_NAME = new ClassPathResource("pmdTextTest.log").getFile().getAbsolutePath();
                                        Map<String, List<PMDStructure>> codeReviewByClass = new HashMap<>();
                                        List<String> stringList = new ArrayList<>();
                                        String branchName = null;
                                        FileInputStream fstream = new FileInputStream(filePath.toString());
                                        try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

                                            String codeReview;
                                            while ((codeReview = br.readLine()) != null) {
                                                if (!codeReview.equals("")) {

                                                    if(codeReview.contains("Checking out Revision")){
                                                        String result[] = codeReview.split("/");
                                                        String s = result[result.length - 1];
                                                        branchName = s.substring(0, s.length()-1);
                                                    }

                                                    if (codeReview.contains(".cls")) {
                                                        stringList.add(codeReview);
                                                    }
                                                }
                                            }

                                            for (String line : stringList) {
                                                String[] split = line.split("\\\\");
                                                codeReviewByClass = createMapOfClassAndReview(split[6], codeReviewByClass, fileName, branchName);

                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if (!codeReviewByClass.isEmpty()) {
                                            Collection<List<PMDStructure>> values = codeReviewByClass.values();
                                            for (List<PMDStructure> value : values) {
                                                List<Document> documentList = changeToValues(value);

                                                salesForceClass.insertMany(documentList);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.print(e);
                }


            }
        } catch (Exception e) {
            build.setResult(Result.UNSTABLE);
        }
        return true;
    }

    private static List<Document> changeToValues(List<PMDStructure> value) {
        List<Document> documentList = new ArrayList<>();
        Document document = null;
        for (PMDStructure pmdStructure : value) {
            document = new Document();
            document.put("classname", pmdStructure.getClassname());
            document.put("date", pmdStructure.getDate());
            document.put("fileName", pmdStructure.getFileName());
            document.put("lineNumber", pmdStructure.getLineNumber());
            document.put("reviewFeedback", pmdStructure.getReviewFeedback());
            document.put("dsalesforceID", pmdStructure.getSalesforceID());
            document.put("branchName", pmdStructure.getBranchName());
            documentList.add(document);
        }

        return documentList;
    }

    private static Map<String, List<PMDStructure>> createMapOfClassAndReview(String line, Map<String, List<PMDStructure>> codeReviewByClass, String fileName, String branchName) {

        String[] classNameAndLineNumber = line.split(":");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);


        if (codeReviewByClass.containsKey(classNameAndLineNumber[0])) {
            List<PMDStructure> pmdStructure = codeReviewByClass.get(classNameAndLineNumber[0]);
            PMDStructure pmdStructure1 = new PMDStructure();
            pmdStructure1.setFileName(fileName);
            pmdStructure1.setClassname(classNameAndLineNumber[0]);
            pmdStructure1.setLineNumber(Integer.valueOf(classNameAndLineNumber[1]));
            pmdStructure1.setReviewFeedback(classNameAndLineNumber[2]);
            pmdStructure1.setDate(format);
            pmdStructure1.setBranchName(branchName);
            pmdStructure.add(pmdStructure1);
        } else {
            List<PMDStructure> pmdStructureList = new ArrayList<>();
            PMDStructure pmdStructure = new PMDStructure();
            pmdStructure.setFileName(fileName);
            pmdStructure.setClassname(classNameAndLineNumber[0]);
            pmdStructure.setLineNumber(Integer.valueOf(classNameAndLineNumber[1]));
            pmdStructure.setReviewFeedback(classNameAndLineNumber[2]);
            pmdStructure.setDate(format);
            pmdStructure.setBranchName(branchName);
            pmdStructureList.add(pmdStructure);
            codeReviewByClass.put(classNameAndLineNumber[0], pmdStructureList);
        }

        return codeReviewByClass;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    /**
     * Descriptor for {@link SaveLogsPublisher}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     * <p>
     * <p>
     * See <tt>src/main/resources/hudson/plugins/ConsoleLogToWorkspace/ConsoleLogToWorkspacePublisher/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Save to Mongo DB";
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types
            return true;
        }

    }

}

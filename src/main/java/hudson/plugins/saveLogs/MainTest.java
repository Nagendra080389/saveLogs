package hudson.plugins.saveLogs;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import hudson.model.Result;
import org.bson.Document;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class MainTest {
    /*public static void main(String[] args) {
        try {

            MongoClient mongoClient = MongoConnection.getMongoConnection("localhost", "27017");
            MongoDatabase pmdReview = mongoClient.getDatabase("pmdReview");
            MongoCollection<Document> salesForceClass = pmdReview.getCollection("SalesForceClass");

            // Read the logs
            final File folder = new File("C:\\Jenkins\\Logs");
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
                                FileInputStream fstream = new FileInputStream(filePath.toString());
                                try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream))) {

                                    String codeReview;
                                    while ((codeReview = br.readLine()) != null) {
                                        if (!codeReview.equals("")) {
                                            if (codeReview.contains(".cls")) {
                                                stringList.add(codeReview);
                                            }
                                        }
                                    }

                                    for (String line : stringList) {
                                        String[] split = line.split("\\\\");
                                        codeReviewByClass = createMapOfClassAndReview(split[6], codeReviewByClass, fileName);

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
            document.put("salesforceID", pmdStructure.getSalesforceID());
            documentList.add(document);
        }

        return documentList;
    }

    private static Map<String, List<PMDStructure>> createMapOfClassAndReview(String line, Map<String, List<PMDStructure>> codeReviewByClass, String fileName) {

        String[] classNameAndLineNumber = line.split(":");
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String format = simpleDateFormat.format(date);


        if(codeReviewByClass.containsKey(classNameAndLineNumber[0])){
            List<PMDStructure> pmdStructure = codeReviewByClass.get(classNameAndLineNumber[0]);
            PMDStructure pmdStructure1 = new PMDStructure();
            pmdStructure1.setFileName(fileName);
            pmdStructure1.setClassname(classNameAndLineNumber[0]);
            pmdStructure1.setLineNumber(Integer.valueOf(classNameAndLineNumber[1]));
            pmdStructure1.setReviewFeedback(classNameAndLineNumber[2]);
            pmdStructure1.setDate(format);
            pmdStructure.add(pmdStructure1);
        }else {
            List<PMDStructure> pmdStructureList = new ArrayList<>();
            PMDStructure pmdStructure = new PMDStructure();
            pmdStructure.setFileName(fileName);
            pmdStructure.setClassname(classNameAndLineNumber[0]);
            pmdStructure.setLineNumber(Integer.valueOf(classNameAndLineNumber[1]));
            pmdStructure.setReviewFeedback(classNameAndLineNumber[2]);
            pmdStructure.setDate(format);
            pmdStructureList.add(pmdStructure);
            codeReviewByClass.put(classNameAndLineNumber[0], pmdStructureList);
        }

        return codeReviewByClass;
    }*/
}

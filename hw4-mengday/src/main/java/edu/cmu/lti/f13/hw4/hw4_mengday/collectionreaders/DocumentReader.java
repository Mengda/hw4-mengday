package edu.cmu.lti.f13.hw4.hw4_mengday.collectionreaders;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.lti.f13.hw4.hw4_mengday.typesystems.Document;

public class DocumentReader extends JCasAnnotator_ImplBase {

  private Pattern linePattern = Pattern.compile(".*?(\\r|\\n|\\r\\n)");
  
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {

    // reading sentence from the CAS
    String Document = jcas.getDocumentText();
    
    int pos = 0;
    Matcher matcher = linePattern.matcher(Document);
    
    while(matcher.find(pos)){
      pos = matcher.end();
      String sLine = matcher.group();
      // String sLine = jcas.getDocumentText();

      // TODO: make sure information from text collection are extracted correctly
      ArrayList<String> docInfo = parseDataLine(sLine);

      // This is to make sure that parsing done properly and
      // minimal data for rel,qid,text are available to proceed
      if (docInfo.size() < 3) {
        System.err.println("Not enough information in the line");
        return;
      }

      int rel = Integer.parseInt(docInfo.get(0));
      int qid = Integer.parseInt(docInfo.get(1));
      String txt = docInfo.get(2);

      Document doc = new Document(jcas);
      doc.setText(txt);
      doc.setQueryID(qid);
      // Setting relevance value
      doc.setRelevanceValue(rel);
      doc.addToIndexes();
      doc.setBegin(matcher.start());
      doc.setEnd(pos);

      // Adding populated FeatureStructure to CAS
      jcas.addFsToIndexes(doc);
    }
    
    System.out.println("Reader END!!!");
  }

  public static ArrayList<String> parseDataLine(String line) {
    ArrayList<String> docInfo;

    String[] rec = line.split("[\\t]");
    String sResQid = (rec[0]).replace("qid=", "");
    String sResRel = (rec[1]).replace("rel=", "");

    StringBuffer sResTxt = new StringBuffer();
    for (int i = 2; i < rec.length; i++) {
      sResTxt.append(rec[i]).append(" ");
    }

    docInfo = new ArrayList<String>();
    docInfo.add(sResRel);
    docInfo.add(sResQid);
    docInfo.add(sResTxt.toString());
    return docInfo;
  }

}
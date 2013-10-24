package edu.cmu.lti.f13.hw4.hw4_mengday.annotators;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
//import org.apache.uima.examples.tokenizer.Token;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.IntegerArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.lti.f13.hw4.hw4_mengday.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_mengday.typesystems.Token;
import edu.cmu.lti.f13.hw4.hw4_mengday.utils.Utils;

public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {

    FSIterator<Annotation> iter = jcas.getAnnotationIndex(Document.type).iterator();
    while (iter.hasNext()) {
      //iter.moveToNext();
      Document doc = (Document) iter.next();
      createTermFreqVector(jcas, doc);
    }

  }

  /**
   * 
   * @param jcas
   * @param doc
   */

  private void createTermFreqVector(JCas jcas, Document doc) {

    String docText = doc.getText();

    // TO DO: construct a vector of tokens and update the tokenList in CAS

    String[] wordList = docText.split(" ");
    HashMap<String, Integer> tokenCount = new HashMap<String, Integer>();
    for (String word : wordList) {
      if (!tokenCount.containsKey(word)) {
        tokenCount.put(word, 1);
      } else {
        tokenCount.put(word, tokenCount.get(word) + 1);
      }
    }

    ArrayList<Token> tokenList = new ArrayList<Token>();
    for (String word : tokenCount.keySet()) {
      Token token = new Token(jcas);
      token.setText(word);
      token.setFrequency(tokenCount.get(word));
      tokenList.add(token);
    }
    FSList tokenFSList = Utils.fromCollectionToFSList(jcas, tokenList);
    doc.setTokenList(tokenFSList);
  }

}

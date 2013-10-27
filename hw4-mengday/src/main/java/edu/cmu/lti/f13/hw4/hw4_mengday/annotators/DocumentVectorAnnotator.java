package edu.cmu.lti.f13.hw4.hw4_mengday.annotators;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.lti.f13.hw4.hw4_mengday.typesystems.Document;
import edu.cmu.lti.f13.hw4.hw4_mengday.typesystems.Token;
import edu.cmu.lti.f13.hw4.hw4_mengday.utils.Utils;

public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

  /**
   * For each Document object in the CAS, count its word frequency and update this object.
   */
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {

    FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
    if (iter.isValid()) {      
      iter.moveToNext();
      Document doc = (Document) iter.get();
      createTermFreqVector(jcas, doc);
    }

  }

  /**
   * For each Document doc in JCas jcas, get the word frequency of each word and count their
   * frequency.
   * 
   * @param jcas
   *          The CAS in which the Document doc is in. Need to set Token and TokenList allocated to
   *          this CAS.
   * @param doc
   *          One ``line'' of the input file.
   */

  private void createTermFreqVector(JCas jcas, Document doc) {

    String docText = doc.getText().toLowerCase();

    // TODO: construct a vector of tokens and update the tokenList in CAS

    String[] wordList = docText.split(" ");
    HashMap<String, Integer> tokenCount = new HashMap<String, Integer>();
    for (String word : wordList) {
      String newWord = word;
      if(word.charAt(word.length()-1)<'a' || word.charAt(word.length()-1)>'z'){
        newWord = word.substring(0, word.length()-1);
      }
      //if(Utils.GetStopWordFilter().isStopword(newWord))continue;
      if (!tokenCount.containsKey(newWord)) {
        tokenCount.put(newWord, 1);
      } else {
        tokenCount.put(newWord, tokenCount.get(newWord) + 1);
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

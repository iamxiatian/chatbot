package xiatian.chatbot.util;

import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;//import org.apache.poi.xwpf.extractor.WordExtractor;

public class Test {
    public static void main(String[] args) throws Exception {
        Test t = new Test();
        String f = "/home/xiatian/workspace/github/chatbot/data/病历20190102/G112607.docx";
        OPCPackage pack = POIXMLDocument.openPackage(f);
        XWPFDocument doc = new XWPFDocument(pack);
        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
        System.out.println(extractor.getText());

        //t.printSdtContent("/home/xiatian/workspace/github/chatbot/data/病历20190102/G112607.docx");
    }

}

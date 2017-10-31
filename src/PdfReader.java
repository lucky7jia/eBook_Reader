import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


public class PdfReader {

    @SuppressWarnings("deprecation")
	 
    
    public static void main(String[] args){

        File pdfFile = new File("/Users/chenyongjia/Desktop/PDFbox/book.pdf");
        PDDocument document = null;
        try
        {
            
            /**
            InputStream input = null;
            input = new FileInputStream( pdfFile );
            //加载 pdf 文档
            PDFParser parser = new PDFParser(new RandomAccessBuffer(input));
            parser.parse();
            document = parser.getPDDocument();
            **/
        		
        		
        	
            
            document=PDDocument.load(pdfFile);
            
            
            
            // 获取页码
            int pages = document.getNumberOfPages();

            // 读文本内容
            PDFTextStripper stripper=new PDFTextStripper();
            // 设置按顺序输出
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(pages);
            String content = stripper.getText(document);
            System.out.println(content);
             
			
			try {
				org.apache.commons.io.FileUtils.writeStringToFile(new File("/Users/chenyongjia/Desktop/PDFbox/book.txt"), content);
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
       

    }
     

}
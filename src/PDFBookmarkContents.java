import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JOptionPane;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.pdfbox.pdmodel.PDDocumentInformation; 

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class PDFBookmarkContents 
{
	public static void main( String[] args ) throws IOException
    {  
		File pdfFile = new File("/Users/chenyongjia/Desktop/PDFbox/Books/Mastering Apache Spark.pdf");
		PDDocument document = null;
		try
		{
			document=PDDocument.load(pdfFile);
			//PrintBookmarks meta = new PrintBookmarks();
			PDDocumentOutline outline =  document.getDocumentCatalog().getDocumentOutline();
			
			
			PDDocumentInformation info = document.getDocumentInformation();
			if( outline != null )
			{
				System.out.println( "标题:" + info.getTitle() );     
				System.out.println( "主题:" + info.getSubject() );     
			  	System.out.println( "作者:" + info.getAuthor() );     
			  	System.out.println( "关键字:" + info.getKeywords() );          
			}
			
			ArrayList<Integer> pageNum=new ArrayList<Integer>();
			ArrayList<Integer> out=bookmarksPageNum(document,outline,"",pageNum);
			System.out.println(out.toString());
			getContent(out,document);
			 
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
     

	public static ArrayList<Integer> bookmarksPageNum(PDDocument document, PDOutlineNode bookmark, String indentation,ArrayList<Integer> output) throws IOException
    {
		//ArrayList<Integer> output = new ArrayList<Integer>();
		PDOutlineItem current = bookmark.getFirstChild();
		while( current != null )
        {
            if (current.getDestination() instanceof PDPageDestination)
            {
                PDPageDestination pd = (PDPageDestination) current.getDestination();
                int desPage= pd.retrievePageNumber()+1;
                System.out.println(desPage);
                output.add(desPage);
            }
            
            if (current.getAction() instanceof PDActionGoTo)
            {
                PDActionGoTo gta = (PDActionGoTo) current.getAction();
                if (gta.getDestination() instanceof PDPageDestination)
                {
                    PDPageDestination pd = (PDPageDestination) gta.getDestination();
                    int desPage2=pd.retrievePageNumber() + 1;
                    output.add(desPage2);
                    System.out.println(desPage2);
                }
            }
            System.out.println( indentation + current.getTitle() );
            bookmarksPageNum( document, current, indentation + "    ", output );
            current = current.getNextSibling();
        }
        return output;     
    }

	public static void getContent (ArrayList<Integer> output,PDDocument input) throws IOException
    {

    		String encoding = "UTF-8";
    		try
    		{ 
    			System.out.println("开始写到txt文件中");
    			for(int i = 0; i <=output.size()-1; i++)
    			{
    			    //System.out.println(i);
    				Writer outgo = null;
				File outputFile = null;
				int startpage=output.get(i);
				int endpage=0;
				if (i==output.size()-1) {
					//System.out.println("hello_end");
					endpage = input.getNumberOfPages();
				} else {
					endpage=output.get(i+1)-1;
				}
				outputFile = new File("/Users/chenyongjia/Desktop/PDFbox/txt/",String.valueOf(startpage)+".txt");
				outgo = new OutputStreamWriter(new FileOutputStream(outputFile),encoding);
				System.out.println("新文件绝对路径为："+outputFile.getAbsolutePath());
				//int pages = input.getNumberOfPages();
				PDFTextStripper stripper=new PDFTextStripper();
				stripper.setSortByPosition(true);
				stripper.setStartPage(startpage);
				stripper.setEndPage(endpage);
				String content = stripper.getText(input);
				//System.out.println(content);
				System.out.println("开始调用writeText方法");
				outgo.write(content);
				outgo.flush();
				outgo.close();
				System.out.println("调用writeText方法结束");
					
			}
    		}
    		catch(Exception e)
    		{
    			System.out.println(e);
    		}
    
    }
   
 
}
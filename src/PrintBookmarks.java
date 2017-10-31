import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

public class PrintBookmarks
{
    public static void main( String[] args ) throws IOException
    {
    		
    	File pdfFile = new File("/Users/chenyongjia/Desktop/PDFbox/Books/Fast Data Processing with Spark.pdf");
    	PDDocument document = null;  
            try
            
            {
             	document=PDDocument.load(pdfFile);
                PrintBookmarks meta = new PrintBookmarks();
                PDDocumentOutline outline =  document.getDocumentCatalog().getDocumentOutline();
                if( outline != null )
                {
                    meta.printBookmark(document, outline, "");
                }
                else
                {
                    System.out.println( "This document does not contain any bookmarks" );
                }
            } 
            catch(Exception e)
            {
                System.out.println(e);
            }
        
    }

    /**
     * This will print the usage for this document.
     */
    private static void usage()
    {
        System.err.println( "Usage: java " + PrintBookmarks.class.getName() + " <input-pdf>" );
    }

    /**
     * This will print the documents bookmarks to System.out.
     *
     * @param document The document.
     * @param bookmark The bookmark to print out.
     * @param indentation A pretty printing parameter
     *
     * @throws IOException If there is an error getting the page count.
     */
    public void printBookmark(PDDocument document, PDOutlineNode bookmark, String indentation) throws IOException
    {
        PDOutlineItem current = bookmark.getFirstChild();
        while( current != null )
        {
            if (current.getDestination() instanceof PDPageDestination)
            {
                PDPageDestination pd = (PDPageDestination) current.getDestination();
                System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
            }
            if (current.getAction() instanceof PDActionGoTo)
            {
                PDActionGoTo gta = (PDActionGoTo) current.getAction();
                if (gta.getDestination() instanceof PDPageDestination)
                {
                    PDPageDestination pd = (PDPageDestination) gta.getDestination();
                    System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
                }
            }
            System.out.println( indentation + current.getTitle() );
            printBookmark( document, current, indentation + "    " );
            current = current.getNextSibling();
        }
    }
}
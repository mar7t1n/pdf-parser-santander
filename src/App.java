
import java.io.File;
import java.nio.file.Files;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import java.util.ArrayList;
import java.util.List;

public class App {

  private static final String workingpath = "E:\\Martin\\Finances\\Santandar\\tosort\\";
  private static List<String> listoffiles;

  public static void main(String[] args) {
    try {
      java.util.logging.Logger.getLogger("org.apache.pdfbox").setLevel(java.util.logging.Level.OFF);

      listoffiles = new ArrayList<>();
      getListOfFile();
      listoffiles.forEach(fn -> {
        try {
          String ac = readpdf(fn);

          File src = new File(fn);
          File tgt = new File(workingpath + ac);

          if (ac != null) {
            // Display command
            System.out.println("move " + src.getPath() + " " + tgt.getPath());
            // Action move file
            Files.move(src.toPath(), tgt.toPath());
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private static void getListOfFile() throws Exception {
    File folder = new File(workingpath);
    File[] listOfFiles = folder.listFiles();

    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        listoffiles.add(listOfFiles[i].getAbsolutePath());
      }
    }
  }

  private static String readpdf(String pdfname) throws Exception {
    PDDocument document = null;
    document = PDDocument.load(new File(pdfname));
    document.getClass();
    if (!document.isEncrypted()) {
      PDFTextStripperByArea stripper = new PDFTextStripperByArea();
      stripper.setSortByPosition(true);
      PDFTextStripper Tstripper = new PDFTextStripper();
      String st = Tstripper.getText(document);
      document.close();
      return getAccount(st);
    } else
      return null;
  }

  private static String getAccount(String docStr) {
    String[] split = docStr.split("\\r?\\n");

    for (String strTemp : split) {
      if (strTemp.startsWith("Account number:")) {

        // Account number: 12345678 Sort Code: 91 21 36 Statement number: 12/2021

        String newfilename = strTemp.substring(16, 24) + "-" + strTemp.substring(66, 70) + "-"
            + strTemp.substring(63, 65) + ".pdf";
        return newfilename;
      }
    }
    return null;

  }
}

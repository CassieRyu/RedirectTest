import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.FileFunctions;
import utils.HTTPUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SiteMapTest {

    @Test
    public void testSiteMap() throws IOException, ParserConfigurationException, SAXException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, URISyntaxException {
        String sitemapFilePath = "src/Files/SiteMap_insurance.xml";
        String resultPath = "src/Files/SiteMap_result_insurance.csv";

        String fileHeader = "test_result, http_response_code, url\n";
        FileFunctions.createFile(resultPath, fileHeader);

        List<String> urls = parseXML(sitemapFilePath);
     //   List<String> urls = FileFunctions.readFile(sitemapFilePath);
        for (String url : urls)
        {
            int responseCode = getURLResponse(url);
            System.out.println(responseCode);
            writeToResultFile(url, responseCode, resultPath);

        //   writeToResultFile(url,resultPath);

        }
    }

    private static List<String> parseXML(String filePath) throws ParserConfigurationException, IOException, SAXException
    {
        File file = new File (filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbFactory.newDocumentBuilder();
        Document doc = db.parse(file);

        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("loc");
        List<String> urls = new ArrayList<>();

       for(int i = 0; i < nodeList.getLength(); i++)
       {
           Node node = nodeList.item(i);
           urls.add(node.getTextContent().trim());
       }
       return urls;
    }

    private int getURLResponse(String url) throws URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        URI urlLink = new URI(url);
        CloseableHttpClient client = HTTPUtils.getClientNoRedirect();
        HttpGet get = new HttpGet(urlLink);

        return client.execute(get).getStatusLine().getStatusCode();
    }

    private static void writeToResultFile(String url, Integer responseCode, String filePath)
    {
        try{
            Boolean success = HTTPUtils.accessSuccess(responseCode);
            FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String  line = success + ", " + responseCode + "," + url + "\n";
            bufferedWriter.write(line);
            bufferedWriter.close();

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void writeToResultFile(String url, String filePath){
        try{

            FileWriter fileWriter = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String  line = url + "\n";
            bufferedWriter.write(line);
            bufferedWriter.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
